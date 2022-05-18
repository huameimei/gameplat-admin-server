package com.gameplat.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.gameplat.admin.model.dto.GameBalanceQueryDTO;
import com.gameplat.admin.model.dto.GameKickOutDTO;
import com.gameplat.admin.model.dto.OperGameTransferRecordDTO;
import com.gameplat.admin.model.vo.GameBalanceVO;
import com.gameplat.admin.model.vo.GameKickOutVO;
import com.gameplat.admin.model.vo.GameRecycleVO;
import com.gameplat.admin.service.*;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.DateUtil;
import com.gameplat.common.constant.CacheKey;
import com.gameplat.common.enums.*;
import com.gameplat.common.enums.PushMessageEnum.MessageCategory;
import com.gameplat.common.enums.PushMessageEnum.MessageType;
import com.gameplat.common.enums.PushMessageEnum.UserRange;
import com.gameplat.common.game.GameBizBean;
import com.gameplat.common.game.TransferResource;
import com.gameplat.common.game.api.GameApi;
import com.gameplat.common.game.exception.GameException;
import com.gameplat.common.game.exception.GameNoRollbackTransferException;
import com.gameplat.common.game.exception.GameRollbackException;
import com.gameplat.common.lang.Assert;
import com.gameplat.common.util.CNYUtils;
import com.gameplat.model.entity.game.GameAmountControl;
import com.gameplat.model.entity.game.GamePlatform;
import com.gameplat.model.entity.game.GameTransferRecord;
import com.gameplat.model.entity.member.Member;
import com.gameplat.model.entity.member.MemberBill;
import com.gameplat.model.entity.member.MemberInfo;
import com.gameplat.model.entity.message.Message;
import com.gameplat.redis.api.RedisService;
import com.gameplat.redis.redisson.DistributedLocker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class GameAdminServiceImpl implements GameAdminService {

  public static final String USER_GAME_BALANCE = "user_game_balance_";

  public static final String USER_GAME_KICKOUT = "user_game_kickOut_";

  @Autowired private ApplicationContext applicationContext;

  @Autowired private MemberInfoService memberInfoService;

  @Autowired private MemberBillService memberBillService;

  @Autowired private GameTransferRecordService gameTransferRecordService;

  @Lazy @Autowired private MemberService memberService;

  @Autowired private GameTransferInfoService gameTransferInfoService;

  @Autowired private GameConfigService gameConfigService;

  @Autowired private MessageInfoService messageInfoService;

  @Autowired private GameAmountControlService gameAmountControlService;

  @Autowired private DistributedLocker distributedLocker;

  @Autowired private GamePlatformService gamePlatformService;

  @Autowired private Executor asyncGameExecutor;

  @Autowired private RedisService redisService;

  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public GameApi getGameApi(String platformCode) {
    GameApi api =
        applicationContext.getBean(platformCode.toLowerCase() + GameApi.SUFFIX, GameApi.class);
    TransferTypesEnum tt = TransferTypesEnum.get(platformCode);
    // 1代表是否额度转换
    if (tt == null || tt.getType() != 1) {
      throw new ServiceException("游戏未接入");
    }
    return api;
  }

  /** 没收玩家游戏平台金钱 */
  @Override
  public void confiscated(String platformCode, Member member, BigDecimal amount) throws Exception {
    GameApi gameApi = getGameApi(platformCode);

    GameBizBean gameBizBean = new GameBizBean();
    gameBizBean.setParams(null);
    String orderNo = gameApi.generateOrderNo(gameBizBean);
    gameBizBean.setGameAccount(member.getGameAccount());
    gameBizBean.setAccount(member.getAccount());
    gameBizBean.setAmount(amount.negate());
    gameBizBean.setOrderNo(orderNo);
    gameBizBean.setTransferIn(platformCode);
    gameBizBean.setConfig(gameConfigService.queryGameConfigInfoByPlatCode(platformCode));
    try {
      log.info("没收玩家{}在{}游戏平台的所有余额,没收金额为{}。", member.getAccount(), platformCode, amount);
      gameApi.transfer(gameBizBean);
      /*
       *  推送站内消息
       */
      Message message = new Message();
      message.setTitle("没收玩家游戏平台余额");
      message.setContent(
          String.format(
              "此次操作为,没收玩家在%s游戏平台的所有余额,没收金额为%s", GamePlatformEnum.getName(platformCode), amount));
      message.setCategory(MessageCategory.SYS_SEND.getValue());
      message.setPushRange(UserRange.SOME_MEMBERS.getValue());
      message.setLinkAccount(member.getAccount());
      message.setType(MessageType.SYSTEM_INFORMATION.value());
      message.setStatus(TrueFalse.TRUE.getValue());
      message.setRemarks("没收玩家游戏平台余额");
      messageInfoService.save(message);
    } catch (Exception e) {
      log.error("没收玩家游戏平台余额失败：{}", e.getMessage());
      throw new ServiceException("没收玩家游戏平台余额失败");
    }
  }

  /** 额度转换 */
  @Override
  public void transfer(
      String transferOut, String transferIn, BigDecimal amount, Member member, Boolean isWeb)
      throws Exception {
    if (amount == null) {
      throw new ServiceException("转换金额不能为空");
    }
    if (TransferTypesEnum.isSelfOut(transferOut, transferIn)) {
      this.transferOut(transferIn, amount, member, false);
    } else {
      this.transferIn(transferOut, amount, member, isWeb, false);
    }
  }

  /** 游戏补单 */
  @Override
  public void fillOrders(OperGameTransferRecordDTO gameTransferRecord) throws Exception {
    GameTransferRecord record = gameTransferRecordService.getById(gameTransferRecord.getId());
    // 订单不存在
    Assert.notNull(record, "额度转换订单不存在");
    if (GameTransferStatus.SUCCESS.getValue().equals(record.getStatus())
        || GameTransferStatus.CANCEL.getValue().equals(record.getStatus())
        || GameTransferStatus.ROLL_BACK.getValue().equals(record.getStatus())) {
      throw new ServiceException("已处理,无需再次补单");
    }
    // 订单有效期为7天，如超过7天未处理，由人工手动审核补
    if (DateUtil.daysBetween(record.getCreateTime(), new Date()) > 7) {
      throw new ServiceException("订单有效期为7天，已过效，请人工核查。");
    }
    Member member =
        memberService
            .getByAccount(gameTransferRecord.getAccount())
            .orElseThrow(() -> new ServiceException("会员账号不存在!"));
    GameApi gameApi = getGameApi(gameTransferRecord.getPlatformCode());
    int status = record.getStatus();
    // 查询转账是否成功
    GameBizBean gameBizBean =
        GameBizBean.builder()
            .orderNo(record.getOrderNo())
            .gameAccount(member.getGameAccount())
            .account(member.getAccount())
            .amount(
                status == GameTransferStatus.IN.getValue()
                    ? record.getAmount().negate()
                    : record.getAmount())
            .config(
                gameConfigService.queryGameConfigInfoByPlatCode(
                    gameTransferRecord.getPlatformCode()))
            .build();
    boolean isSuccess = gameApi.queryOrderStatus(gameBizBean);
    if (isSuccess) {
      // 额度转换已成功更新记录
      if (status == GameTransferStatus.OUT.getValue()) {
        gameTransferRecord.setStatus(GameTransferStatus.SUCCESS.getValue());
        gameTransferRecord.setRemark(record.getRemark() + "，已审核，游戏平台已转入。");
        gameTransferRecordService.fillOrders(gameTransferRecord);
        // 游戏转出成功，平台未转入
      } else if (status == GameTransferStatus.IN.getValue()
          || status == GameTransferStatus.IN_GAME_FAIL.getValue()) {
        // 更新状态
        gameTransferRecord.setStatus(GameTransferStatus.SUCCESS.getValue());
        gameTransferRecord.setRemark(record.getRemark() + "，已审核，游戏平台已转出。");
        gameTransferRecordService.fillOrders(gameTransferRecord);
        // 查账变记录是否有此定单
        MemberBill memberBill =
            memberBillService.queryGameMemberBill(
                member.getId(), gameTransferRecord.getOrderNo(), TranTypes.GAME_IN.getValue());
        if (memberBill == null) {
          memberBill = new MemberBill();
          // 转入系统
          MemberInfo memberInfo = memberInfoService.getById(member.getId());
          Assert.notNull(memberInfo, "请查询会员是否存在");
          // 平台入款操作
          memberInfoService.updateBalance(member.getId(), record.getAmount());
          memberBill.setBalance(memberInfo.getBalance());
          memberBill.setAmount(record.getAmount());
          memberBill.setTranType(TranTypes.GAME_IN.getValue());
          memberBill.setOrderNo(record.getOrderNo());
          String content =
              "订单："
                  + record.getOrderNo()
                  + "补单。"
                  + GamePlatformEnum.getName(record.getPlatformCode())
                  + "转系统，金额："
                  + CNYUtils.formatYuanAsYuan(record.getAmount())
                  + "，转入前系统余额："
                  + CNYUtils.formatYuanAsYuan(memberInfo.getBalance())
                  + "，"
                  + Objects.requireNonNull(TransferTypesEnum.get(record.getPlatformCode()))
                      .getName()
                  + "："
                  + CNYUtils.formatYuanAsYuan(record.getBalance());
          memberBill.setContent(content);
          memberBill.setRemark("游戏平台转出成功，平台转入失败，后台补单");
          memberBillService.save(member, memberBill);
        }
      }
    } else {
      MemberInfo memberInfo = memberInfoService.getById(member.getId());
      Assert.notNull(memberInfo, "请查询会员是否存在");
      // 平台转出成功，补真人
      if (status == GameTransferStatus.OUT.getValue()) {
        gameTransferRecord.setStatus(GameTransferStatus.SUCCESS.getValue());
        gameTransferRecord.setRemark(record.getRemark() + "，已审核，平台已出，补真人。");
        // 游戏平台是的余额查询
        GameBizBean bizBean =
            GameBizBean.builder()
                .gameAccount(member.getGameAccount())
                .account(member.getAccount())
                .platformCode(gameTransferRecord.getPlatformCode())
                .config(
                    gameConfigService.queryGameConfigInfoByPlatCode(
                        gameTransferRecord.getPlatformCode()))
                .build();
        BigDecimal balance = gameApi.getBalance(bizBean);
        String orderNo = record.getOrderNo();
        MemberBill bill = new MemberBill();
        bill.setBalance(memberInfo.getBalance());
        bill.setAmount(BigDecimal.ZERO.negate());
        bill.setTranType(TranTypes.GAME_OUT.getValue());
        bill.setOrderNo(orderNo);
        String content =
            "订单："
                + orderNo
                + "补单。"
                + "系统转"
                + GamePlatformEnum.getName(record.getPlatformCode())
                + "，金额："
                + CNYUtils.formatYuanAsYuan(record.getAmount())
                + "，转入前系统余额："
                + CNYUtils.formatYuanAsYuan(memberInfo.getBalance())
                + "，"
                + Objects.requireNonNull(TransferTypesEnum.get(record.getPlatformCode())).getName()
                + "："
                + CNYUtils.formatYuanAsYuan(balance);
        bill.setContent(content);
        bill.setRemark("平台转出成功，游戏转入失败，后台补单");
        gameTransferRecordService.fillOrders(gameTransferRecord);
        memberBillService.save(member, bill);
        // 自动转换补单需更新游戏额度状态
        if (record.getTransferStatus() != null
            && record.getTransferStatus() == TrueFalse.TRUE.getValue()) {
          gameTransferInfoService.update(member.getId(), record.getPlatformCode(), orderNo);
        }
        bizBean = new GameBizBean();
        bizBean.setGameAccount(member.getGameAccount());
        bizBean.setAmount(record.getAmount());
        bizBean.setOrderNo(orderNo);
        bizBean.setConfig(
            gameConfigService.queryGameConfigInfoByPlatCode(gameTransferRecord.getPlatformCode()));
        gameApi.transfer(bizBean);
      } else if (status == GameTransferStatus.IN_GAME_FAIL.getValue()) {
        // 游戏转出失败
        throw new ServiceException("请让会员重新提交额度转换操作。无需后台进入补单操作。");
      }
    }
  }

  @Override
  // @Cached(name = CachedKeys.GAME_BALANCE_CACHE, key = "#platformCode + '_' + #member.id", expire
  // = 7200)
  public BigDecimal getBalance(String platformCode, Member member) throws Exception {
    if (TransferTypesEnum.SELF.getCode().equals(platformCode)) {
      return memberInfoService.getById(member.getId()).getBalance();
    }
    GameApi gameApi = getGameApi(platformCode);
    // 出款为游戏平台是的余额查询
    try {
      GameBizBean bizBean = new GameBizBean();
      bizBean.setGameAccount(member.getGameAccount());
      bizBean.setAccount(member.getAccount());
      bizBean.setConfig(gameConfigService.queryGameConfigInfoByPlatCode(platformCode));
      return gameApi.getBalance(bizBean);
    } catch (Exception ex) {
      log.error("游戏平台{},获取余额失败:{}", platformCode, ex.getMessage());
    }
    return BigDecimal.ZERO;
  }

  /**
   * 平台转出,真人转入 全部都是自动转出，把会员的所有余额直接转过去 admin和web都有转入，转出方法， 区别在于金额获取，和转换模式，
   *
   * @param transferType true:自动额度转换的，false: 表示是手动转换
   */
  @Override
  public void transferOut(
      String transferOut, BigDecimal amount, Member member, boolean transferType) throws Exception {
    GameApi gameApi = getGameApi(transferOut);
    // 1.获取账号信息 获取余额
    MemberInfo memberInfo = memberInfoService.getById(member.getId());
    if (transferType) {
      // 自动转换 直接amount就是会员余额
      amount = memberInfo.getBalance();
      if (amount.compareTo(BigDecimal.ZERO) > 0) {
        log.info("会员余额为" + amount);
        return;
      }
    }
    // 转换成功需要增加游戏使用额度
    Object value = redisService.getStringOps().get(CacheKey.GAME_AMOUNT_CONTROL);
    GameAmountControl gameAmountControl;
    if (ObjectUtil.isNotNull(value)) {
      gameAmountControl = JSONUtil.toBean(value.toString(), GameAmountControl.class);
    } else {
      gameAmountControl =
          gameAmountControlService.findInfoByType(GameAmountControlTypeEnum.LIVE.type());
    }
    if (GameAmountControlTypeEnum.checkGameAmountControlType(transferOut)
        && ObjectUtil.isNotNull(gameAmountControl)
        && gameAmountControl
                .getUseAmount()
                .add(amount.abs())
                .compareTo(gameAmountControl.getAmount())
            >= 0) {
      throw new ServiceException("转入金额已经超过剩余可用额度，请联系客服");
    }
    // 转出平台余额小于转账余额则抛出错误信息
    if (memberInfo.getBalance().compareTo(amount) < 0) {
      throw new ServiceException(
          member.getAccount() + " 用户账号余额为：" + memberInfo.getBalance() + "，账户余额不足");
    }
    // 2.查询转入游戏平台前的余额
    GameBizBean gameBizBean =
        GameBizBean.builder()
            .gameAccount(member.getGameAccount())
            .account(member.getAccount())
            .config(gameConfigService.queryGameConfigInfoByPlatCode(transferOut))
            .build();
    BigDecimal balance = gameApi.getBalance(gameBizBean);
    // 3.平台出款操作
    memberInfoService.updateBalance(memberInfo.getMemberId(), amount.negate());
    gameBizBean = new GameBizBean();
    gameBizBean.setParams(null);
    String orderNo = gameApi.generateOrderNo(gameBizBean);
    // 5. 会员流水记录
    String transferOutName = GamePlatformEnum.getName(transferOut);
    String transferTypeName = Objects.requireNonNull(TransferTypesEnum.get(transferOut)).getName();
    BigDecimal memberBalance = memberInfo.getBalance();

    // 初始化备注信息
    String remark =
        createTransferOutRemark(transferOutName, amount, memberBalance, transferTypeName, balance);

    MemberBill bill = new MemberBill();
    bill.setAmount(amount.negate());
    bill.setOrderNo(orderNo);
    bill.setTranType(TranTypes.GAME_OUT.getValue());

    bill.setContent(remark);
    bill.setBalance(memberInfo.getBalance());
    memberBillService.save(member, bill);

    // 6. 调用第三方游戏的转账接口
    Integer status = GameTransferStatus.OUT.getValue();
    try {
      gameBizBean =
          GameBizBean.builder()
              .gameAccount(member.getGameAccount())
              .account(member.getAccount())
              .amount(amount)
              .orderNo(orderNo)
              .config(gameConfigService.queryGameConfigInfoByPlatCode(transferOut))
              .build();
      gameApi.transfer(gameBizBean);
      status = GameTransferStatus.SUCCESS.getValue();
      // 7. 添加额度转换记录
      gameTransferInfoService.update(memberInfo.getMemberId(), transferOut, orderNo);
    } catch (GameException | GameRollbackException ex) {
      boolean bool = gameApi.queryOrderStatus(gameBizBean);
      if (bool) {
        status = GameTransferStatus.SUCCESS.getValue();
      } else {
        if (ex instanceof GameNoRollbackTransferException) {
          remark += "系统转出成功，" + transferOutName + "转入失败";
        } else {
          remark = transferOutName + "转入失败,系统转出金额退回";
          status = GameTransferStatus.ROLL_BACK.getValue();
          throw ex;
        }
      }
    } catch (Exception ex) {
      // 游戏转入失败不做事务回滚,平台还是按转出成功处理,因为可以会有网络问题,游戏可能已转入成功
      log.error(
          "系统转出成功，" + transferOutName + "转入失败，金额：" + amount + "，会员账号：" + member.getAccount(), ex);
      remark += "系统转出成功，" + GamePlatformEnum.getName(transferOut) + "转入失败";
    } finally {
      GameTransferRecord transferRecord = new GameTransferRecord();
      transferRecord.setAmount(amount);
      transferRecord.setStatus(status);
      transferRecord.setTransferType(TransferTypesEnum.OUT.getType());
      transferRecord.setAccount(member.getAccount());
      transferRecord.setBalance(balance);
      transferRecord.setOrderNo(orderNo);
      transferRecord.setMemberId(member.getId());
      transferRecord.setPlatformCode(transferOut);
      transferRecord.setRemark(remark);
      transferRecord.setTransferStatus(
          transferType ? TrueFalse.TRUE.getValue() : TrueFalse.FALSE.getValue());
      gameTransferRecordService.saveTransferRecord(transferRecord);
    }
  }

  public String createTransferOutRemark(
      String transferOutName,
      BigDecimal amount,
      BigDecimal memberBalance,
      String transferTypeName,
      BigDecimal balance) {
    return "系统转"
        + transferOutName
        + "，金额："
        + CNYUtils.formatYuanAsYuan(amount)
        + "，转出前系统余额："
        + CNYUtils.formatYuanAsYuan(memberBalance)
        + "，"
        + transferTypeName
        + "："
        + CNYUtils.formatYuanAsYuan(balance);
  }

  /**
   * 游戏转出,平台转入
   *
   * @param amount 回收都是全部余额回收 不管金额
   * @param transferType true表示免额度转换的，false表示是手动转
   */
  @Override
  public void transferIn(
      String transferIn, BigDecimal amount, Member member, boolean isWeb, boolean transferType)
      throws Exception {
    // 1.获取账号信息
    String account = member.getAccount();
    GameApi gameApi = getGameApi(transferIn);

    // 2.获取会员在游戏平台的余额
    GameBizBean gameBizBean =
        GameBizBean.builder()
            .gameAccount(member.getGameAccount())
            .account(member.getAccount())
            .config(gameConfigService.queryGameConfigInfoByPlatCode(transferIn))
            .build();
    BigDecimal balance = gameApi.getBalance(gameBizBean);
    // 自动转动实际上回收的是在第三方游戏的所有余额
    if (transferType) {
      amount = balance;
      if (balance.compareTo(BigDecimal.ZERO) < 0) {
        log.info(transferIn + "游戏余额不足,不能转出" + balance);
        return;
      }
    }
    MemberInfo memberInfo = memberInfoService.getById(member.getId());
    String orderNo = gameApi.generateOrderNo(gameBizBean);
    String transferInName = GamePlatformEnum.getName(transferIn);
    String transferTypeName = Objects.requireNonNull(TransferTypesEnum.get(transferIn)).getName();
    StringBuilder remark =
        new StringBuilder(
            createTransferInRemark(
                transferInName, amount, memberInfo.getBalance(), transferTypeName, balance));
    Integer status = GameTransferStatus.SUCCESS.getValue();
    // 3.调用平台API进行转账
    TransferResource transferResource;
    try {
      try {
        gameBizBean.setAmount(balance.negate());
        gameBizBean.setOrderNo(orderNo);
        transferResource = gameApi.transfer(gameBizBean);
        if (transferResource.getOrderNo() != null) {
          orderNo = transferResource.getOrderNo();
        }
      } catch (Exception ex) {
        boolean bool = false;
        if (ex instanceof GameException) {
          // 查询订单是否成功
          bool = gameApi.queryOrderStatus(gameBizBean);
        }
        if (!bool) {
          status = GameTransferStatus.IN_GAME_FAIL.getValue();
          remark.append(",").append(transferInName).append("转出失败");
          throw ex;
        }
      }
      try {
        // 4. 我们平台入款操作
        memberInfoService.updateBalance(memberInfo.getMemberId(), amount);
        // 5. 记录现金流水
        MemberBill bill = new MemberBill();
        bill.setAmount(amount);
        bill.setOrderNo(orderNo);
        bill.setTranType(TranTypes.GAME_IN.getValue());
        bill.setContent(remark.toString());
        bill.setBalance(memberInfo.getBalance());
        memberBillService.save(member, bill);
        remark.append("，系统转入成功");
        // 自动转换需要更新余额所在的游戏信息
        if (transferType) {
          gameTransferInfoService.update(
              memberInfo.getMemberId(), TransferTypesEnum.SELF.getCode(), orderNo);
        }
      } catch (Exception ex) {
        remark.append("，系统转入失败");
        status = GameTransferStatus.IN.getValue();
        log.error(remark.toString(), ex);
        throw new ServiceException(remark.toString());
      }
    } catch (Exception ex) {
      throw new ServiceException(ex);
    } finally {
      GameTransferRecord transferRecord = new GameTransferRecord();
      transferRecord.setAmount(amount);
      transferRecord.setStatus(status);
      transferRecord.setAccount(account);
      transferRecord.setBalance(balance);
      transferRecord.setTransferType(TransferTypesEnum.IN.getType());
      transferRecord.setOrderNo(orderNo);
      transferRecord.setMemberId(member.getId());
      transferRecord.setPlatformCode(transferIn);
      transferRecord.setRemark(remark.toString());
      transferRecord.setTransferStatus(
          transferType ? TrueFalse.TRUE.getValue() : TrueFalse.FALSE.getValue());
      gameTransferRecordService.saveTransferRecord(transferRecord);
    }
  }

  public String createTransferInRemark(
      String transferInName,
      BigDecimal amount,
      BigDecimal memberBalance,
      String transferTypeName,
      BigDecimal balance) {
    return transferInName
        + "转系统，金额："
        + CNYUtils.formatYuanAsYuan(amount)
        + "，转入前系统余额："
        + CNYUtils.formatYuanAsYuan(memberBalance)
        + ","
        + transferTypeName
        + ":"
        + CNYUtils.formatYuanAsYuan(balance);
  }

  /** 一键回收 */
  @Override
  public List<GameRecycleVO> recyclingAmountByAccount(String account) {
    Assert.notEmpty(account, "会员账号不能为空");
    Member member = memberService.getMemberAndFillGameAccount(account);
    Assert.notNull(member, "会员账号不存在，请重新检查");
    log.error("{}一键回收游戏平台额度操作频繁", account);
    String key = USER_GAME_BALANCE + member.getId();
    distributedLocker.lock(key, TimeUnit.SECONDS, 300);
    try {
      List<GamePlatform> playedGamePlatform = this.getPlayedPlatform(member.getId());
      log.info("游戏平台额度转换通道中:{}", playedGamePlatform.size());
      if (CollectionUtil.isEmpty(playedGamePlatform)) {
        return new ArrayList();
      }
      List<CompletableFuture<GameRecycleVO>> futures =
          this.batchRecovery(playedGamePlatform, member);
      // 等待异步任务完成
      CompletableFuture.allOf(futures.toArray(new CompletableFuture[] {})).join();
      return futures.stream().map(this::getRecoveryResult).collect(Collectors.toList());
    } finally {
      distributedLocker.unlock(key);
    }
  }

  private GameRecycleVO getRecoveryResult(CompletableFuture<GameRecycleVO> future) {
    try {
      return future.get();
    } catch (InterruptedException | ExecutionException ex) {
      ex.printStackTrace();
    }
    return null;
  }

  private List<CompletableFuture<GameRecycleVO>> batchRecovery(
      List<GamePlatform> playedGamePlatform, Member member) {
    return playedGamePlatform.stream()
        .map(platform -> this.asyncRecovery(platform, member))
        .collect(Collectors.toList());
  }

  private CompletableFuture<GameRecycleVO> asyncRecovery(GamePlatform platform, Member member) {
    return CompletableFuture.supplyAsync(
        () -> doRecoveryGameBalance(platform, member), asyncGameExecutor);
  }

  private GameRecycleVO doRecoveryGameBalance(GamePlatform platform, Member member) {
    GameRecycleVO gameRecycleVO =
        GameRecycleVO.builder()
            .platformName(platform.getName())
            .platformCode(platform.getCode())
            .status(ResultStatusEnum.SUCCESS.getValue()) // 默认成功
            .build();
    try {
      BigDecimal amount =
          this.getBalance(platform.getCode(), member).setScale(2, RoundingMode.DOWN);
      gameRecycleVO.setBalance(amount);
      if (amount.compareTo(BigDecimal.ZERO) > 0) {
        this.transfer(platform.getCode(), TransferTypesEnum.SELF.getCode(), amount, member, false);
        log.info("游戏额度回收:会员账号:{}，游戏编码：{},游戏金额：{}", member.getAccount(), platform.getCode(), amount);
      } else {
        gameRecycleVO.setErrorMsg(platform.getCode() + "游戏余额不足，忽略操作");
        gameRecycleVO.setStatus(ResultStatusEnum.WARNING.getValue());
        // 记录日志
        log.info(
            "游戏额度回收:会员账号:{}，游戏编码：{},游戏金额：{},忽略操作", member.getAccount(), platform.getCode(), amount);
      }
    } catch (Exception e) {
      log.error("回收游戏金额失败：{}", e.getMessage());
      gameRecycleVO.setStatus(ResultStatusEnum.FAILED.getValue());
      gameRecycleVO.setErrorMsg("回收游戏金额失败");
    }
    return gameRecycleVO;
  }

  /** 一键查询 */
  @Override
  public List<GameBalanceVO> selectGameBalanceByAccount(String account) {
    Assert.notEmpty(account, "会员账号不能为空");
    Member member = memberService.getMemberAndFillGameAccount(account);
    Assert.notNull(member, "会员账号不存在，请重新检查");
    String key = USER_GAME_BALANCE + member.getId();
    distributedLocker.lock(key, TimeUnit.SECONDS, 300);
    log.info("{}一键回收游戏平台额度操作:", account);
    try {
      List<GamePlatform> playedGamePlatform = this.getPlayedPlatform(member.getId());
      if (CollectionUtil.isEmpty(playedGamePlatform)) {
        throw new ServiceException("游戏平台额度转换通道维护中");
      }
      List<CompletableFuture<GameBalanceVO>> futures =
          this.batchQueryBalance(playedGamePlatform, member);
      // 等待异步任务完成
      CompletableFuture.allOf(futures.toArray(new CompletableFuture[] {})).join();
      return futures.stream().map(this::getQueryBalanceResult).collect(Collectors.toList());
    } finally {
      distributedLocker.unlock(key);
    }
  }

  private GameBalanceVO getQueryBalanceResult(CompletableFuture<GameBalanceVO> future) {
    try {
      return future.get();
    } catch (InterruptedException | ExecutionException ex) {
      ex.printStackTrace();
    }
    return null;
  }

  private List<CompletableFuture<GameBalanceVO>> batchQueryBalance(
      List<GamePlatform> playedGamePlatform, Member member) {
    return playedGamePlatform.stream()
        .map(platform -> this.asyncQueryBalance(platform, member))
        .collect(Collectors.toList());
  }

  private CompletableFuture<GameBalanceVO> asyncQueryBalance(GamePlatform platform, Member member) {
    return CompletableFuture.supplyAsync(
        () -> doQueryGameBalance(platform, member), asyncGameExecutor);
  }

  private GameBalanceVO doQueryGameBalance(GamePlatform platform, Member member) {
    GameBalanceVO gameBalanceVO =
        GameBalanceVO.builder()
            .platformCode(platform.getCode())
            .platformName(platform.getName())
            .status(ResultStatusEnum.SUCCESS.getValue())
            .build();
    if (platform.getTransfer() != null
        && platform.getTransfer().equals(TrueFalse.FALSE.getValue())) {
      gameBalanceVO.setErrorMsg(platform.getName() + "查询余额通道正在维护中,请耐心等待");
      gameBalanceVO.setBalance(BigDecimal.ZERO);
      gameBalanceVO.setStatus(ResultStatusEnum.FAILED.getValue());
      return gameBalanceVO;
    }
    try {
      gameBalanceVO.setBalance(
          this.getBalance(platform.getCode(), member).setScale(2, RoundingMode.DOWN));
    } catch (Exception e) {
      log.error("查询余额错误：{}", e.getMessage());
      gameBalanceVO.setBalance(BigDecimal.ZERO);
      gameBalanceVO.setStatus(ResultStatusEnum.FAILED.getValue());
      gameBalanceVO.setErrorMsg("查询游戏余额失败");
    }
    return gameBalanceVO;
  }

  private List<GamePlatform> getPlayedPlatform(Long memberId) {
    List<GamePlatform> allGamePlatform = gamePlatformService.list();
    return this.getPlayedPlatform(allGamePlatform, memberId);
  }

  private List<GamePlatform> getPlayedPlatform(List<GamePlatform> allGamePlatform, Long memberId) {
    List<String> played = this.getPlayedGames(memberId);
    return CollectionUtil.isNotEmpty(played)
        ? allGamePlatform.stream()
            .filter(e -> played.contains(e.getCode()))
            .collect(Collectors.toList())
        : CollectionUtil.empty(ArrayList.class);
  }

  /** 会员进入过的游戏平台 */
  private List<String> getPlayedGames(Long memberId) {
    return gameTransferRecordService.findPlatformCodeList(memberId).stream()
        .map(GameTransferRecord::getPlatformCode)
        .collect(Collectors.toList());
  }

  /** 一键没收 */
  @Override
  public List<GameRecycleVO> confiscatedGameByAccount(String account) {
    Assert.notEmpty(account, "会员账号不能为空");
    Member member = memberService.getMemberAndFillGameAccount(account);
    Assert.notNull(member, "会员账号不存在");
    String key = USER_GAME_BALANCE + member.getId();
    distributedLocker.lock(key, TimeUnit.SECONDS, 300);
    log.info("{}一键没收游戏平台额度操作:", account);
    try {
      List<GamePlatform> playedGamePlatform = this.getPlayedPlatform(member.getId());
      if (CollectionUtil.isEmpty(playedGamePlatform)) {
        throw new ServiceException("游戏平台额度转换通道维护中");
      }
      List<CompletableFuture<GameRecycleVO>> futures =
          this.batchConfiscated(playedGamePlatform, member);
      // 等待异步任务完成
      CompletableFuture.allOf(futures.toArray(new CompletableFuture[] {})).join();
      return futures.stream().map(this::getRecoveryResult).collect(Collectors.toList());
    } finally {
      distributedLocker.unlock(key);
    }
  }

  private List<CompletableFuture<GameRecycleVO>> batchConfiscated(
      List<GamePlatform> playedGamePlatform, Member member) {
    return playedGamePlatform.stream()
        .map(platform -> this.asyncConfiscated(platform, member))
        .collect(Collectors.toList());
  }

  private CompletableFuture<GameRecycleVO> asyncConfiscated(GamePlatform platform, Member member) {
    return CompletableFuture.supplyAsync(() -> doConfiscated(platform, member), asyncGameExecutor);
  }

  private GameRecycleVO doConfiscated(GamePlatform platform, Member member) {
    GameRecycleVO gameRecycleVO =
        GameRecycleVO.builder()
            .platformName(platform.getName())
            .platformCode(platform.getCode())
            .status(ResultStatusEnum.SUCCESS.getValue())
            .build();
    if (platform.getTransfer() != null
        && platform.getTransfer().equals(TrueFalse.FALSE.getValue())) {
      gameRecycleVO.setErrorMsg(platform.getName() + "查询余额通道正在维护中,请耐心等待");
      gameRecycleVO.setStatus(ResultStatusEnum.FAILED.getValue());
      return gameRecycleVO;
    }
    try {
      // 先获取游戏余额
      BigDecimal amount =
          this.getBalance(platform.getCode(), member).setScale(2, RoundingMode.DOWN);
      if (amount.compareTo(BigDecimal.ZERO) > 0) {
        this.confiscated(platform.getCode(), member, amount);
      } else {
        gameRecycleVO.setErrorMsg(platform.getCode() + "游戏余额不足，忽略操作");
        gameRecycleVO.setStatus(ResultStatusEnum.WARNING.getValue());
        log.info("没收玩家{}在{}平台的余额，余额为{}，跳过处理。", member.getAccount(), platform.getCode(), amount);
      }
    } catch (Exception e) {
      log.error("没收游戏余额错误：{}", e.getMessage());
      gameRecycleVO.setStatus(ResultStatusEnum.FAILED.getValue());
      gameRecycleVO.setErrorMsg("没收游戏余额失败");
    }
    return gameRecycleVO;
  }

  @Override
  public GameBalanceVO selectGameBalance(GameBalanceQueryDTO dto) {
    GameBalanceVO gameBalanceVO = new GameBalanceVO();
    Member member = checkGameBalanceParam(dto);
    gameBalanceVO.setPlatformCode(dto.getPlatformCode());
    gameBalanceVO.setStatus(ResultStatusEnum.SUCCESS.getValue());
    try {
      BigDecimal amount =
          this.getBalance(dto.getPlatformCode(), member).setScale(2, RoundingMode.DOWN);
      gameBalanceVO.setBalance(amount);
    } catch (Exception e) {
      log.error("查询失败：{}", e.getMessage());
      gameBalanceVO.setStatus(ResultStatusEnum.FAILED.getValue());
      gameBalanceVO.setErrorMsg("游戏余额查询失败");
    }
    return gameBalanceVO;
  }

  /** 回收余额 */
  @Override
  public GameRecycleVO recyclingAmount(GameBalanceQueryDTO dto) {
    GameRecycleVO gameRecycleVO = new GameRecycleVO();
    Member member = checkGameBalanceParam(dto);
    String key = USER_GAME_BALANCE + member.getId();
    distributedLocker.lock(key, TimeUnit.SECONDS, 300);
    log.info("{}回收游戏平台{}额度操作:", dto.getAccount(), dto.getPlatformCode());
    gameRecycleVO.setPlatformCode(dto.getPlatformCode());
    gameRecycleVO.setStatus(ResultStatusEnum.SUCCESS.getValue());
    try {
      BigDecimal amount =
          this.getBalance(dto.getPlatformCode(), member).setScale(2, RoundingMode.DOWN);
      gameRecycleVO.setBalance(amount);
      if (amount.compareTo(BigDecimal.ZERO) > 0) {
        this.transfer(
            dto.getPlatformCode(), TransferTypesEnum.SELF.getCode(), amount, member, false);
      } else {
        gameRecycleVO.setStatus(ResultStatusEnum.WARNING.getValue());
        gameRecycleVO.setErrorMsg(dto.getPlatformCode() + "游戏余额不足，忽略操作");
        log.info("回收玩家{}在{}平台的余额，余额为{}，跳过处理。", member.getAccount(), dto.getPlatformCode(), amount);
      }
    } catch (Exception e) {
      log.error("回收失败：{}", e.getMessage());
      gameRecycleVO.setStatus(ResultStatusEnum.FAILED.getValue());
      gameRecycleVO.setErrorMsg("游戏余额回收失败");
    } finally {
      distributedLocker.unlock(key);
    }
    return gameRecycleVO;
  }

  /** 没收余额 */
  @Override
  public GameRecycleVO confiscatedAmount(GameBalanceQueryDTO dto) {
    GameRecycleVO gameRecycleVO = new GameRecycleVO();
    Member member = checkGameBalanceParam(dto);
    String key = USER_GAME_BALANCE + member.getId();
    distributedLocker.lock(key, TimeUnit.SECONDS, 300);
    log.info("{}没收游戏平台{}额度操作:", dto.getAccount(), dto.getPlatformCode());
    gameRecycleVO.setPlatformName(dto.getPlatformCode());
    gameRecycleVO.setStatus(ResultStatusEnum.SUCCESS.getValue());
    try {
      // 先获取游戏余额
      BigDecimal amount =
          this.getBalance(dto.getPlatformCode(), member).setScale(2, RoundingMode.DOWN);
      gameRecycleVO.setBalance(amount);
      if (amount.compareTo(BigDecimal.ZERO) > 0) {
        this.confiscated(dto.getPlatformCode(), member, amount);
      } else {
        gameRecycleVO.setErrorMsg(dto.getPlatformCode() + "游戏余额不足，忽略操作");
        gameRecycleVO.setStatus(ResultStatusEnum.WARNING.getValue());
        log.info("没收玩家{}在{}平台的余额，余额为{}，跳过处理。", member.getAccount(), dto.getPlatformCode(), amount);
      }
    } catch (Exception e) {
      log.info(e.getMessage());
      gameRecycleVO.setErrorMsg("没收会员游戏金额失败");
      gameRecycleVO.setStatus(ResultStatusEnum.FAILED.getValue());
    } finally {
      distributedLocker.unlock(key);
    }
    return gameRecycleVO;
  }

  private Member checkGameBalanceParam(GameBalanceQueryDTO dto) {
    Assert.notEmpty(dto.getAccount(), "会员账号不能为空");
    Assert.notEmpty(dto.getPlatformCode(), "游戏平台编码不能为空");
    Member member = memberService.getMemberAndFillGameAccount(dto.getAccount());
    Assert.notNull(member, "会员账号不存在，请重新检查");
    return member;
  }

  @Override
  public void transferToGame(OperGameTransferRecordDTO record) throws Exception {
    String key = "self_" + record.getPlatformCode() + '_' + record.getAccount();
    try {
      distributedLocker.lock(key, TimeUnit.SECONDS, 300);
      Member member = memberService.getMemberAndFillGameAccount(record.getAccount());
      Assert.notNull(member, "会员账号不存在");
      this.transferOut(record.getPlatformCode(), record.getAmount(), member, false);
    } finally {
      distributedLocker.unlock(key);
    }
  }

  @Override
  public List<GameKickOutVO> kickOutAll(GameKickOutDTO dto) {
    Assert.notEmpty(dto.getAccount(), "会员账号不能为空");
    Member member = memberService.getMemberAndFillGameAccount(dto.getAccount());
    Assert.notNull(member, "会员账号不存在，请重新检查");
    String key = USER_GAME_KICKOUT + member.getId();
    boolean flag = distributedLocker.tryLock(key, TimeUnit.SECONDS, 0, 300);
    if (!flag) {
      throw new ServiceException("会员一键踢出太频繁，请稍后再试");
    }
    try {
      List<GamePlatform> playedGamePlatform = this.getPlayedPlatform(member.getId());
      if (CollectionUtil.isEmpty(playedGamePlatform)) {
        return new ArrayList();
      }
      List<CompletableFuture<GameKickOutVO>> futures =
          this.batchGameKickOut(playedGamePlatform, member);
      // 等待异步任务完成
      CompletableFuture.allOf(futures.toArray(new CompletableFuture[] {})).join();
      return futures.stream().map(this::getKickOutResult).collect(Collectors.toList());
    } finally {
      distributedLocker.unlock(key);
    }
  }

  @Override
  public void kickOut(GameKickOutDTO dto) {
    Assert.notEmpty(dto.getAccount(), "会员账号不能为空");
    Assert.notEmpty(dto.getPlatformCode(), "游戏平台编码不能为空");
    Member member = memberService.getMemberAndFillGameAccount(dto.getAccount());
    GameApi gameApi = getGameApi(dto.getPlatformCode());
    GameBizBean gameBizBean = new GameBizBean();
    gameBizBean.setAccount(member.getAccount());
    gameBizBean.setGameAccount(member.getGameAccount());
    gameBizBean.setConfig(gameConfigService.queryGameConfigInfoByPlatCode(dto.getPlatformCode()));
    try {
      gameApi.kickOut(gameBizBean);
    } catch (ServiceException e) {
      throw new ServiceException(e.getMessage());
    } catch (Exception e) {
      log.error("踢出游戏失败", e);
      throw new ServiceException("踢出游戏失败");
    }
  }

  @Override
  public List<GameKickOutVO> batchKickOut(GameKickOutDTO dto) {
    Assert.notEmpty(dto.getAccounts(), "会员账号不能为空");
    Assert.notEmpty(dto.getPlatformCode(), "游戏平台编码不能为空");
    GamePlatform gamePlatform = gamePlatformService.queryByCode(dto.getPlatformCode());
    Assert.notNull(gamePlatform, "未找到游戏平台信息");
    List<String> accountList = Arrays.asList(dto.getAccounts().split(","));
    List<GameKickOutVO> result = Collections.synchronizedList(new ArrayList<>());
    accountList.parallelStream()
        .forEach(
            account -> {
              Member member;
              try {
                member = memberService.getMemberAndFillGameAccount(account);
              } catch (ServiceException e) {
                GameKickOutVO gameKickOutVO =
                    GameKickOutVO.builder()
                        .platformName(gamePlatform.getName())
                        .platformCode(gamePlatform.getCode())
                        .status(ResultStatusEnum.FAILED.getValue())
                        .errorMsg(e.getMessage())
                        .account(account)
                        .build();
                result.add(gameKickOutVO);
                return;
              }
              GameKickOutVO gameKickOutVO = doGameKickOut(gamePlatform, member);
              result.add(gameKickOutVO);
            });
    return result;
  }

  private List<CompletableFuture<GameKickOutVO>> batchGameKickOut(
      List<GamePlatform> playedGamePlatform, Member member) {
    return playedGamePlatform.stream()
        .map(platform -> this.asyncGameKickOut(platform, member))
        .collect(Collectors.toList());
  }

  private CompletableFuture<GameKickOutVO> asyncGameKickOut(GamePlatform platform, Member member) {
    return CompletableFuture.supplyAsync(() -> doGameKickOut(platform, member), asyncGameExecutor);
  }

  private GameKickOutVO doGameKickOut(GamePlatform platform, Member member) {
    GameKickOutVO gameKickOutVO =
        GameKickOutVO.builder()
            .platformName(platform.getName())
            .platformCode(platform.getCode())
            .status(ResultStatusEnum.SUCCESS.getValue()) // 默认成功
            .account(member.getAccount())
            .build();
    try {
      GameApi gameApi = getGameApi(platform.getCode());
      GameBizBean gameBizBean = new GameBizBean();
      gameBizBean.setAccount(member.getAccount());
      gameBizBean.setGameAccount(member.getGameAccount());
      gameBizBean.setConfig(gameConfigService.queryGameConfigInfoByPlatCode(platform.getCode()));
      gameApi.kickOut(gameBizBean);
    } catch (ServiceException e) {
      gameKickOutVO.setStatus(ResultStatusEnum.FAILED.getValue());
      gameKickOutVO.setErrorMsg(e.getMessage());
    } catch (Exception e) {
      log.error("踢出游戏失败", e);
      gameKickOutVO.setStatus(ResultStatusEnum.FAILED.getValue());
      gameKickOutVO.setErrorMsg("踢出游戏失败");
    }
    return gameKickOutVO;
  }

  private GameKickOutVO getKickOutResult(CompletableFuture<GameKickOutVO> future) {
    try {
      return future.get();
    } catch (InterruptedException | ExecutionException ex) {
      ex.printStackTrace();
    }
    return null;
  }
}
