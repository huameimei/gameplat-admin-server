package com.gameplat.admin.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.gameplat.admin.model.dto.OperGameTransferRecordDTO;
import com.gameplat.admin.model.vo.GameBalanceVO;
import com.gameplat.admin.model.vo.GameConfiscatedVO;
import com.gameplat.admin.model.vo.GameRecycleVO;
import com.gameplat.admin.service.GameAdminService;
import com.gameplat.admin.service.GameAmountControlService;
import com.gameplat.admin.service.GameConfigService;
import com.gameplat.admin.service.GamePlatformService;
import com.gameplat.admin.service.GameTransferInfoService;
import com.gameplat.admin.service.GameTransferRecordService;
import com.gameplat.admin.service.MemberBillService;
import com.gameplat.admin.service.MemberInfoService;
import com.gameplat.admin.service.MemberService;
import com.gameplat.admin.service.MessageInfoService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.DateUtil;
import com.gameplat.common.enums.GameAmountControlTypeEnum;
import com.gameplat.common.enums.GamePlatformEnum;
import com.gameplat.common.enums.GameTransferStatus;
import com.gameplat.common.enums.PushMessageEnum.MessageCategory;
import com.gameplat.common.enums.PushMessageEnum.MessageType;
import com.gameplat.common.enums.PushMessageEnum.UserRange;
import com.gameplat.common.enums.ResultStatusEnum;
import com.gameplat.common.enums.TranTypes;
import com.gameplat.common.enums.TransferTypesEnum;
import com.gameplat.common.enums.TrueFalse;
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
import com.google.common.collect.Lists;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;

@Slf4j
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class GameAdminServiceImpl implements GameAdminService {

  @Resource private ApplicationContext applicationContext;
  @Resource private MemberInfoService memberInfoService;
  @Resource private MemberBillService memberBillService;
  @Resource private GameTransferRecordService gameTransferRecordService;
  @Resource private MemberService memberService;
  @Resource private GameTransferInfoService gameTransferInfoService;
  @Resource private GameConfigService gameConfigService;
  @Resource private MessageInfoService messageInfoService;
  @Resource private GameAmountControlService gameAmountControlService;
  @Resource private RedisService redisService;
  @Resource private GamePlatformService gamePlatformService;
  @Resource private Executor asyncGameExecutor;

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
    gameApi.isOpen();
    GameBizBean gameBizBean = new GameBizBean();
    gameBizBean.setParams(null);
    String orderNo = gameApi.generateOrderNo(gameBizBean);
    gameBizBean.setGameAccount(member.getGameAccount());
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
      message.setStatus(1);
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

  /** 真人补单 */
  @Override
  public void fillOrders(OperGameTransferRecordDTO gameTransferRecord) throws Exception {
    GameTransferRecord record = gameTransferRecordService.getById(gameTransferRecord.getId());
    // 订单不存在
    if (record == null) {
      throw new ServiceException("订单不存在");
    }
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
    GameBizBean gameBizBean = new GameBizBean();
    gameBizBean.setOrderNo(record.getOrderNo());
    gameBizBean.setGameAccount(member.getGameAccount());
    gameBizBean.setAmount(
        status == GameTransferStatus.IN.getValue()
            ? record.getAmount().negate()
            : record.getAmount());
    gameBizBean.setConfig(
        gameConfigService.queryGameConfigInfoByPlatCode(gameTransferRecord.getPlatformCode()));
    boolean isSuccess = gameApi.queryOrderStatus(gameBizBean);
    if (isSuccess) {
      // 额度转换已成功更新记录
      if (status == GameTransferStatus.OUT.getValue()) {
        gameTransferRecord.setStatus(GameTransferStatus.SUCCESS.getValue());
        gameTransferRecord.setRemark(record.getRemark() + "，已审核，游戏平台已转入。");
        gameTransferRecordService.fillOrders(gameTransferRecord);
        // 真人转出成功，平台未转入
      } else if (status == GameTransferStatus.IN.getValue()
          || status == GameTransferStatus.IN_GAME_FAIL.getValue()) {
        // 更新状态
        gameTransferRecord.setStatus(GameTransferStatus.SUCCESS.getValue());
        gameTransferRecord.setRemark(record.getRemark() + "，已审核，游戏平台已转出。");
        gameTransferRecordService.fillOrders(gameTransferRecord);
        // 查账变记录是否有此定单
        MemberBill memberBill =
            memberBillService.queryLiveBill(
                member.getId(), gameTransferRecord.getOrderNo(), TranTypes.GAME_IN.getValue());
        if (memberBill == null) {
          memberBill = new MemberBill();
          // 转入系统
          MemberInfo memberInfo = memberInfoService.getById(member.getId());
          if (memberInfo == null) {
            throw new ServiceException("请查询会员余额是否存在");
          }
          // 平台入款操作
          memberInfoService.updateBalanceWithRecharge(
              member.getId(), record.getAmount(), record.getAmount());
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
      if (memberInfo == null) {
        throw new ServiceException("请查询会员余额是否存在");
      }
      // 平台转出成功，补真人
      if (status == GameTransferStatus.OUT.getValue()) {
        gameTransferRecord.setStatus(GameTransferStatus.SUCCESS.getValue());
        gameTransferRecord.setRemark(record.getRemark() + "，已审核，平台已出，补真人。");
        // 游戏平台是的余额查询
        GameBizBean bizBean = new GameBizBean();
        bizBean.setGameAccount(member.getGameAccount());
        bizBean.setPlatformCode(gameTransferRecord.getPlatformCode());
        bizBean.setConfig(
            gameConfigService.queryGameConfigInfoByPlatCode(gameTransferRecord.getPlatformCode()));
        BigDecimal balance = gameApi.getBalance(bizBean);
        String orderNo = record.getOrderNo();
        MemberBill bill = new MemberBill();
        bill.setBalance(memberInfo.getBalance());
        bill.setAmount(BigDecimal.ONE.negate());
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
        bill.setRemark("平台转出成功，真人转入失败，后台补单");
        gameTransferRecordService.fillOrders(gameTransferRecord);
        memberBillService.save(member, bill);
        // 自动转换补单需更新真人额度状态
        if (record.getTransferStatus() != null && record.getTransferStatus() == 1) {
          gameTransferInfoService.update(member.getId(), record.getPlatformCode(), orderNo);
        }
        bizBean = new GameBizBean();
        bizBean.setGameAccount(member.getGameAccount());
        bizBean.setAmount(record.getAmount());
        bizBean.setOrderNo(orderNo);
        bizBean.setConfig(
            gameConfigService.queryGameConfigInfoByPlatCode(gameTransferRecord.getPlatformCode()));
        gameApi.transfer(bizBean);
        // 真人转出失败
      } else if (status == GameTransferStatus.IN_GAME_FAIL.getValue()) {
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
    gameApi.isOpen();
    // 出款为游戏平台是的余额查询
    try {
      GameBizBean bizBean = new GameBizBean();
      bizBean.setGameAccount(member.getGameAccount());
      bizBean.setConfig(gameConfigService.queryGameConfigInfoByPlatCode(platformCode));
      return gameApi.getBalance(bizBean);
    } catch (Exception ex) {
      log.error("游戏平台{},获取余额失败:{}", platformCode, ex.getMessage());
    }
    return BigDecimal.ZERO;
  }

  /**
   * 平台转出,真人转入 全部都是自动转出，把会员的所有余额直接转过去
   *
   * @param transferType true:自动额度转换的，false: 表示是手动转换
   */
  @Override
  public void transferOut(
      String transferOut, BigDecimal amount, Member member, boolean transferType) throws Exception {
    GameApi gameApi = getGameApi(transferOut);
    gameApi.isOpen();
    // 1.获取账号信息 获取余额
    MemberInfo memberInfo = memberInfoService.getById(member.getId());
    if (transferType) {
      // 自动转换 直接amount就是会员余额
      amount = memberInfo.getBalance();
      if (amount.compareTo(BigDecimal.ZERO) > 0) {
        log.info("余额为" + amount);
        return;
      }
    }
    // 转换成功需要增加游戏使用额度
    GameAmountControl gameAmountControl =
        gameAmountControlService.findInfoByType(
            GameAmountControlTypeEnum.checkGameAmountControlType(transferOut));
    if (ObjectUtil.isNotNull(gameAmountControl)
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
    // 2.查询转入游戏平台的余额
    GameBizBean gameBizBean = new GameBizBean();
    gameBizBean.setGameAccount(member.getGameAccount());
    gameBizBean.setConfig(gameConfigService.queryGameConfigInfoByPlatCode(transferOut));
    BigDecimal balance = gameApi.getBalance(gameBizBean);
    // 3.平台出款操作
    memberInfoService.updateBalanceWithWithdraw(memberInfo.getMemberId(), amount);
    gameBizBean = new GameBizBean();
    gameBizBean.setParams(null);
    String orderNo = gameApi.generateOrderNo(gameBizBean);
    // 5. 会员流水记录
    MemberBill bill = new MemberBill();
    bill.setAmount(amount.negate());
    bill.setOrderNo(orderNo);
    bill.setTranType(TranTypes.GAME_OUT.getValue());
    String content =
        "系统转"
            + GamePlatformEnum.getName(transferOut)
            + "，金额："
            + CNYUtils.formatYuanAsYuan(amount)
            + "，转出前系统余额："
            + CNYUtils.formatYuanAsYuan(memberInfo.getBalance())
            + "，"
            + Objects.requireNonNull(TransferTypesEnum.get(transferOut)).getName()
            + "："
            + CNYUtils.formatYuanAsYuan(balance);
    bill.setContent(content);
    bill.setBalance(memberInfo.getBalance());
    memberBillService.save(member, bill);

    // 6. 调用第三方游戏的转账接口
    Integer status = GameTransferStatus.OUT.getValue();
    String remark = "";
    try {
      gameBizBean = new GameBizBean();
      gameBizBean.setGameAccount(member.getGameAccount());
      gameBizBean.setAmount(amount);
      gameBizBean.setOrderNo(orderNo);
      gameBizBean.setConfig(gameConfigService.queryGameConfigInfoByPlatCode(transferOut));
      gameApi.transfer(gameBizBean);
      remark =
          "系统转"
              + GamePlatformEnum.getName(transferOut)
              + "，金额："
              + CNYUtils.formatYuanAsYuan(amount)
              + "，转出前系统余额："
              + CNYUtils.formatYuanAsYuan(memberInfo.getBalance())
              + ","
              + Objects.requireNonNull(TransferTypesEnum.get(transferOut)).getName()
              + ":"
              + CNYUtils.formatYuanAsYuan(balance);
      status = GameTransferStatus.SUCCESS.getValue();
      // 7. 添加额度转换记录
      gameTransferInfoService.update(memberInfo.getMemberId(), transferOut, orderNo);
      // 8. 增加转出额度数据
      gameAmountControl.setUseAmount(gameAmountControl.getUseAmount().add(amount.abs()));
      gameAmountControlService.updateById(gameAmountControl);
    } catch (GameException | GameRollbackException ex) {
      boolean bool = gameApi.queryOrderStatus(gameBizBean);
      if (bool) {
        remark =
            "系统转"
                + GamePlatformEnum.getName(transferOut)
                + "，金额："
                + CNYUtils.formatYuanAsYuan(amount)
                + "，转出前系统余额："
                + CNYUtils.formatYuanAsYuan(memberInfo.getBalance())
                + ","
                + Objects.requireNonNull(TransferTypesEnum.get(transferOut)).getName()
                + ":"
                + CNYUtils.formatYuanAsYuan(balance);
        status = GameTransferStatus.SUCCESS.getValue();
      } else {
        if (ex instanceof GameNoRollbackTransferException) {
          remark += "系统转出成功，" + GamePlatformEnum.getName(transferOut) + "转入失败";
        } else {
          remark = GamePlatformEnum.getName(transferOut) + "转入失败,系统转出金额退回";
          status = GameTransferStatus.ROLL_BACK.getValue();
          throw ex;
        }
      }
    } catch (Exception ex) {
      // 真人转入失败不做事务回滚,平台还是按转出成功处理,因为可以会有网络问题,真人可能已转入成功
      // logger.error("系统转出成功，" + LiveGame.getName(transferIn) + "转入失败，金额：" + amount + "，会员账号：" +
      // userInfo.getAccount(), ex);
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
      transferRecord.setTransferStatus(transferType ? 1 : 0);
      saveTransferRecord(transferRecord);
    }
  }

  /**
   * 真人转出,平台转入
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
    gameApi.isOpen();
    // 2.获取会员在游戏平台的余额
    GameBizBean gameBizBean = new GameBizBean();
    gameBizBean.setGameAccount(member.getGameAccount());
    gameBizBean.setConfig(gameConfigService.queryGameConfigInfoByPlatCode(transferIn));
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
    gameBizBean = new GameBizBean();
    gameBizBean.setParams(null);
    String orderNo = gameApi.generateOrderNo(gameBizBean);
    StringBuilder remark = new StringBuilder();
    remark
        .append(GamePlatformEnum.getName(transferIn))
        .append("转系统，金额：")
        .append(CNYUtils.formatYuanAsYuan(amount))
        .append("，转入前系统余额：")
        .append(CNYUtils.formatYuanAsYuan(memberInfo.getBalance()))
        .append(",")
        .append(Objects.requireNonNull(TransferTypesEnum.get(transferIn)).getName())
        .append(":")
        .append(CNYUtils.formatYuanAsYuan(balance));
    Integer status = GameTransferStatus.SUCCESS.getValue();
    // 3.调用平台API进行转账
    TransferResource transferResource;
    try {
      try {
        gameBizBean = new GameBizBean();
        gameBizBean.setGameAccount(member.getGameAccount());
        gameBizBean.setAmount(balance.negate());
        gameBizBean.setOrderNo(orderNo);
        gameBizBean.setConfig(gameConfigService.queryGameConfigInfoByPlatCode(transferIn));
        transferResource = gameApi.transfer(gameBizBean);
        if (transferResource.getOrderNo() != null) {
          orderNo = transferResource.getOrderNo();
        }
      } catch (Exception ex) {
        boolean b = false;
        if (ex instanceof GameException) {
          // 查询订单是否成功
          b = gameApi.queryOrderStatus(gameBizBean);
        }
        if (!b) {
          status = GameTransferStatus.IN_GAME_FAIL.getValue();
          remark.append(",").append(GamePlatformEnum.getName(transferIn)).append("转出失败");
          throw ex;
        }
      }
      try {
        // 4. 我们平台入款操作
        memberInfoService.updateBalanceWithRecharge(memberInfo.getMemberId(), amount, amount);
        // 5. 记录现金流水
        MemberBill bill = new MemberBill();
        bill.setAmount(amount);
        bill.setOrderNo(orderNo);
        bill.setTranType(TranTypes.GAME_IN.getValue());
        String content =
            GamePlatformEnum.getName(transferIn)
                + "转系统，金额："
                + CNYUtils.formatYuanAsYuan(amount)
                + "，转入前系统余额："
                + CNYUtils.formatYuanAsYuan(memberInfo.getBalance())
                + "，"
                + Objects.requireNonNull(TransferTypesEnum.get(transferIn)).getName()
                + "："
                + CNYUtils.formatYuanAsYuan(balance);
        bill.setContent(content);
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
      transferRecord.setTransferStatus(transferType ? 1 : 0);
      saveTransferRecord(transferRecord);
    }
  }

  @Async
  public void saveTransferRecord(GameTransferRecord transferRecord) {
    try {
      log.info(
          "真人插入数据:"
              + transferRecord.getAccount()
              + ",金额:"
              + transferRecord.getAmount()
              + ",订单号:"
              + transferRecord.getOrderNo()
              + ",备注:"
              + transferRecord.getRemark());
      gameTransferRecordService.save(transferRecord);
    } catch (Exception e) {
      log.error("真人插入数据:订单号=" + transferRecord.getOrderNo(), e);
    }
  }

  @NotNull
  private List<GamePlatform> getGamePlatformList(Member member) {
    List<GameTransferRecord> recordList =
        gameTransferRecordService.findPlatformCodeList(member.getId());
    List<String> platformList = new ArrayList<>();
    recordList.forEach(item -> platformList.add(item.getPlatformCode()));
    List<GamePlatform> gamePlatformList = gamePlatformService.list();
    // 仅获取当前会员玩过得游戏
    return gamePlatformList.stream()
        .filter(item -> platformList.contains(item.getCode()))
        .collect(Collectors.toList());
  }

  /** 一键回收 */
  @Override
  public List<GameRecycleVO> recyclingAmountByAccount(String account) {
    Assert.notEmpty(account, "会员账号不能为空");
    Member member = memberService.getMemberAndFillGameAccount(account);
    Assert.notNull(member, "会员账号不存在，请重新检查");
    List<GamePlatform> playedPlatformList = getGamePlatformList(member);
    if (CollectionUtils.isEmpty(playedPlatformList)) {
      throw new ServiceException("游戏平台额度转换通道维护中");
    }
    String key = "user_game_balance_" + member.getId();
    try {
      redisService.getStringOps().setEx(key, "user_game_balance", 300, TimeUnit.SECONDS);
    } catch (Exception e) {
      log.error("{}一键回收游戏平台额度操作频繁", account);
      throw new ServiceException("一键回收游戏平台额度操作频繁");
    }
    List<GameRecycleVO> resultList = new ArrayList<>();
    try {
      StopWatch stopwatch = new StopWatch();
      stopwatch.start();
      // 每10个切分成一个list去请求
      List<List<GamePlatform>> allList = Lists.partition(playedPlatformList, 10);
      for (List<GamePlatform> partList : allList) {
        int size = partList.size();
        CompletableFuture<GameRecycleVO>[] arrays = new CompletableFuture[size];
        for (int i = 0; i < size; i++) {
          GamePlatform item = partList.get(i);
          CompletableFuture<GameRecycleVO> completableFuture =
              CompletableFuture.supplyAsync(
                  () -> {
                    GameRecycleVO gameRecycleVO = new GameRecycleVO();
                    // 获取真人信息
                    gameRecycleVO.setPlatformCode(item.getCode());
                    gameRecycleVO.setPlatformName(item.getName());
                    gameRecycleVO.setStatus(ResultStatusEnum.SUCCESS.getValue()); // 默认成功
                    try {
                      BigDecimal amount = this.getBalance(item.getCode(), member);
                      gameRecycleVO.setBalance(amount);
                      if (amount.compareTo(BigDecimal.ZERO) > 0) {
                        this.transfer(
                            item.getCode(),
                            TransferTypesEnum.SELF.getCode(),
                            amount,
                            member,
                            false);
                        log.info(
                            "游戏额度回收:会员账号:{}，游戏编码：{},游戏金额：{}",
                            member.getAccount(),
                            item.getCode(),
                            amount);
                      } else {
                        gameRecycleVO.setErrorMsg(item.getCode() + "游戏余额不足，忽略操作");
                        gameRecycleVO.setStatus(ResultStatusEnum.WARNING.getValue());
                        // 记录日志
                        log.info(
                            "游戏额度回收:会员账号:{}，游戏编码：{},游戏金额：{},忽略操作",
                            member.getAccount(),
                            item.getCode(),
                            amount);
                      }
                    } catch (Exception e) {
                      log.error("回收游戏金额失败：{}", e.getMessage());
                      gameRecycleVO.setStatus(ResultStatusEnum.FAILED.getValue());
                      gameRecycleVO.setErrorMsg("回收游戏金额失败");
                    }
                    return gameRecycleVO;
                  });
          arrays[i] = completableFuture;
        }
        // 等待10次异步请求结果
        CompletableFuture.allOf(arrays).join();
        Arrays.stream(arrays)
            .forEach(
                item -> {
                  try {
                    resultList.add(item.get());
                  } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                  }
                });
      }
      stopwatch.stop();
    } finally {
      redisService.getKeyOps().delete(key);
    }
    return resultList;
  }

  /** 一键查询 */
  @Override
  public List<GameBalanceVO> selectGameBalanceByAccount(String account) {
    List<GameBalanceVO> resultList = new ArrayList<>();
    Assert.notEmpty(account, "会员账号不能为空");
    Member member = memberService.getMemberAndFillGameAccount(account);
    Assert.notNull(member, "会员账号不存在，请重新检查");

    List<GamePlatform> playedPlatformList = getGamePlatformList(member);
    if (CollectionUtils.isEmpty(playedPlatformList)) {
      throw new ServiceException("游戏平台额度转换通道维护中");
    }

    String key = "user_game_balance_" + member.getId();
    try {
      redisService.getStringOps().setEx(key, "user_game_balance", 300, TimeUnit.SECONDS);
    } catch (Exception e) {
      log.error("{}一键回收游戏平台额度操作频繁", account);
      throw new ServiceException("一键回收游戏平台额度操作频繁");
    }
    try {
      StopWatch stopwatch = new StopWatch();
      stopwatch.start();
      // 每10个切分成一个list去请求
      List<List<GamePlatform>> allList = Lists.partition(playedPlatformList, 10);
      for (List<GamePlatform> partList : allList) {
        int size = partList.size();
        CompletableFuture<GameBalanceVO>[] arrays = new CompletableFuture[size];
        for (int i = 0; i < size; i++) {
          GamePlatform item = partList.get(i);
          CompletableFuture<GameBalanceVO> completableFuture =
              CompletableFuture.supplyAsync(
                  () -> {
                    GameBalanceVO gameBalanceVO = new GameBalanceVO();
                    // 获取游戏信息
                    gameBalanceVO.setPlatformCode(item.getCode());
                    gameBalanceVO.setPlatformName(item.getName());
                    gameBalanceVO.setStatus(ResultStatusEnum.SUCCESS.getValue());
                    if (item.getTransfer() != null
                        && item.getTransfer().equals(TrueFalse.FALSE.getValue())) {
                      gameBalanceVO.setErrorMsg(item.getName() + "查询余额通道正在维护中,请耐心等到");
                      gameBalanceVO.setBalance(BigDecimal.ZERO);
                      gameBalanceVO.setStatus(ResultStatusEnum.FAILED.getValue());
                      return gameBalanceVO;
                    }
                    try {
                      gameBalanceVO.setBalance(this.getBalance(item.getCode(), member));
                    } catch (Exception e) {
                      log.error("查询余额错误：{}", e.getMessage());
                      gameBalanceVO.setBalance(BigDecimal.ZERO);
                      gameBalanceVO.setStatus(ResultStatusEnum.FAILED.getValue());
                      gameBalanceVO.setErrorMsg("查询游戏余额失败");
                    }
                    return gameBalanceVO;
                  },
                  asyncGameExecutor);
          arrays[i] = completableFuture;
        }
        // 等待10次异步请求结果
        CompletableFuture.allOf(arrays).join();
        Arrays.stream(arrays)
            .forEach(
                item -> {
                  try {
                    resultList.add(item.get());
                  } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                  }
                });
      }
      stopwatch.stop();
    } finally {
      redisService.getKeyOps().delete(key);
    }
    return resultList;
  }

  /** 一键没收 */
  @Override
  public List<GameConfiscatedVO> confiscatedGameByAccount(String account) {
    List<GameConfiscatedVO> resultList = new ArrayList<>();
    Assert.notEmpty(account, "会员账号不能为空");
    Member member = memberService.getMemberAndFillGameAccount(account);
    Assert.notNull(member, "会员账号不存在");
    List<GamePlatform> playedPlatformList = getGamePlatformList(member);

    String key = "user_game_balance_" + member.getId();
    try {
      redisService.getStringOps().setEx(key, "user_game_balance", 300, TimeUnit.SECONDS);
    } catch (Exception e) {
      log.error("{}一键没收游戏平台额度操作频繁", account);
      throw new ServiceException("一键没收游戏平台额度操作频繁");
    }

    try {
      StopWatch stopwatch = new StopWatch();
      stopwatch.start();
      // 每10个切分成一个list去请求
      List<List<GamePlatform>> allList = Lists.partition(playedPlatformList, 10);

      for (List<GamePlatform> partList : allList) {
        int size = partList.size();
        CompletableFuture<GameConfiscatedVO>[] arrays = new CompletableFuture[size];
        for (int i = 0; i < size; i++) {
          GamePlatform item = partList.get(i);
          CompletableFuture<GameConfiscatedVO> completableFuture =
              CompletableFuture.supplyAsync(
                  () -> {
                    GameConfiscatedVO gameConfiscatedVO = new GameConfiscatedVO();
                    gameConfiscatedVO.setPlatformCode(item.getCode());
                    gameConfiscatedVO.setPlatformName(item.getName());
                    gameConfiscatedVO.setStatus(ResultStatusEnum.SUCCESS.getValue());

                    if (item.getTransfer() != null
                        && item.getTransfer().equals(TrueFalse.FALSE.getValue())) {
                      gameConfiscatedVO.setErrorMsg(item.getName() + "查询余额通道正在维护中,请耐心等到");
                      gameConfiscatedVO.setStatus(ResultStatusEnum.FAILED.getValue());
                      return gameConfiscatedVO;
                    }
                    try {
                      // 先获取游戏余额
                      BigDecimal amount = this.getBalance(item.getCode(), member);
                      if (amount.compareTo(BigDecimal.ZERO) > 0) {
                        this.confiscated(item.getCode(), member, amount);
                      } else {
                        gameConfiscatedVO.setErrorMsg(item.getCode() + "游戏余额不足，忽略操作");
                        gameConfiscatedVO.setStatus(ResultStatusEnum.WARNING.getValue());
                        log.info(
                            "没收玩家{}在{}平台的余额，余额为{}，跳过处理。",
                            member.getAccount(),
                            item.getCode(),
                            amount);
                      }
                    } catch (Exception e) {
                      log.error("没收游戏余额错误：{}", e.getMessage());
                      gameConfiscatedVO.setStatus(ResultStatusEnum.FAILED.getValue());
                      gameConfiscatedVO.setErrorMsg("没收游戏余额失败");
                    }
                    return gameConfiscatedVO;
                  },
                  asyncGameExecutor);
          arrays[i] = completableFuture;
        }
        // 等待10次异步请求结果
        CompletableFuture.allOf(arrays).join();
        Arrays.stream(arrays)
            .forEach(
                item -> {
                  try {
                    resultList.add(item.get());
                  } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                  }
                });
      }
      stopwatch.stop();
    } finally {
      redisService.getKeyOps().delete(key);
    }
    return resultList;
  }
}
