package com.gameplat.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.constant.MemberServiceKeyConstant;
import com.gameplat.admin.convert.MemberWealConvert;
import com.gameplat.admin.convert.MessageInfoConvert;
import com.gameplat.admin.enums.LanguageEnum;
import com.gameplat.admin.enums.PushMessageEnum;
import com.gameplat.admin.mapper.*;
import com.gameplat.admin.model.dto.*;
import com.gameplat.admin.model.vo.MemberWealVO;
import com.gameplat.admin.service.*;
import com.gameplat.base.common.context.GlobalContextHolder;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.IPUtils;
import com.gameplat.base.common.util.RandomUtil;
import com.gameplat.common.enums.BooleanEnum;
import com.gameplat.common.enums.TranTypes;
import com.gameplat.common.lang.Assert;
import com.gameplat.model.entity.ValidWithdraw;
import com.gameplat.model.entity.member.*;
import com.gameplat.model.entity.message.Message;
import com.gameplat.model.entity.message.MessageDistribute;
import com.gameplat.redis.redisson.DistributedLocker;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.gameplat.common.enums.TranTypes.*;
import static java.util.stream.Collectors.toList;

/**
 * 福利发放业务处理
 *
 * @author lily
 * @date 2021/11/22
 */
@Service
@Slf4j
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class MemberWealServiceImpl extends ServiceImpl<MemberWealMapper, MemberWeal>
    implements MemberWealService {
  @Autowired private MemberWealConvert wealConvert;

  @Autowired private MemberWealMapper memberWealMapper;

  @Autowired private MemberGrowthRecordMapper memberGrowthRecordMapper;

  @Autowired private RechargeOrderMapper rechargeOrderMapper;

  @Autowired private GameMemberReportMapper dayReportMapper;

  @Autowired private MemberMapper memberMapper;

  @Autowired private MemberBlackListService blackListService;

  @Autowired private MemberWealDetailService wealDetailService;

  @Autowired private MemberGrowthConfigMapper growthConfigMapper;

  @Autowired private MemberGrowthLevelMapper growthLevelMapper;

  @Autowired private ValidWithdrawService validWithdrawService;

  @Autowired private DistributedLocker distributedLocker;

  @Autowired private MemberWealRewordService wealRewordService;

  @Autowired private RedissonClient redissonClient;

  @Autowired private MessageMapper messageMapper;

  @Autowired private MessageDistributeService messageDistributeService;

  @Autowired private MemberInfoService memberInfoService;

  @Autowired private MemberBillService memberBillService;

  @Autowired private MessageInfoConvert messageInfoConvert;

  @Autowired private MemberService memberService;

  @Override
  public IPage<MemberWealVO> findMemberWealList(IPage<MemberWeal> page, MemberWealDTO queryDTO) {
    return this.lambdaQuery()
        .like(ObjectUtils.isNotEmpty(queryDTO.getName()), MemberWeal::getName, queryDTO.getName())
        .eq(
            ObjectUtils.isNotEmpty(queryDTO.getStatus()),
            MemberWeal::getStatus,
            queryDTO.getStatus())
        .eq(ObjectUtils.isNotEmpty(queryDTO.getType()), MemberWeal::getType, queryDTO.getType())
        .ge(
            ObjectUtils.isNotEmpty(queryDTO.getStartDate()),
            MemberWeal::getCreateTime,
            queryDTO.getStartDate())
        .le(
            ObjectUtils.isNotEmpty(queryDTO.getEndDate()),
            MemberWeal::getCreateTime,
            queryDTO.getEndDate())
        .orderByDesc(MemberWeal::getCreateTime)
        .page(page)
        .convert(wealConvert::toVo);
  }

  @Override
  public void addMemberWeal(MemberWealAddDTO dto) {
    if (StringUtils.isBlank(dto.getName())) {
      throw new ServiceException("名称不能为空！");
    }
    if (dto.getType() == null) {
      throw new ServiceException("类型不能为空！");
    }
    if (dto.getStartDate() == null || dto.getEndDate() == null) {
      throw new ServiceException("周期不能为空！");
    }
    if (dto.getMinRechargeAmount() == null) {
      throw new ServiceException("最低充值金额不能为空！");
    }
    if (dto.getMinBetAmount() == null) {
      throw new ServiceException("最低有效投注不能为空！");
    }
    dto.setStatus(0);

    Assert.isTrue(this.save(wealConvert.toEntity(dto)), "新增失败!");
  }

  @Override
  public void updateMemberWeal(MemberWealEditDTO dto) {
    if (ObjectUtils.isEmpty(dto.getId())) {
      throw new ServiceException("id不能为空!");
    }
    Assert.isTrue(this.updateById(wealConvert.toEntity(dto)), "编辑失败!");
  }

  @Override
  public void deleteMemberWeal(Long id) {
    if (ObjectUtils.isEmpty(id)) {
      throw new ServiceException("id不能为空!");
    }
    Assert.isTrue(this.removeById(id), "删除失败!");
  }

  @Override
  public void settleWeal(Long id) {
    if (ObjectUtils.isEmpty(id)) {
      throw new ServiceException("id不能为空!");
    }
    MemberWeal memberWeal = memberWealMapper.selectById(id);
    if (memberWeal == null) {
      throw new ServiceException("该福利不存在!");
    }
    // 福利类型
    Integer type = memberWeal.getType();
    // 福利状态
    Integer status = memberWeal.getStatus();
    if (status != 0 && status != 1) {
      throw new IllegalArgumentException("福利状态异常,不能结算！");
    }
    // 获取会员对应等级的福利金额
    List<MemberWealDetail> memberSalaryInfoList =
        memberGrowthRecordMapper.getMemberSalaryInfo(type);

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    // 查找符合充值金额和打码量资格的会员
    List<String> rechargeAccountList = new ArrayList<>();
    if (memberWeal.getMinRechargeAmount() != null
        && memberWeal.getMinRechargeAmount().compareTo(new BigDecimal("0")) > 0) {
      rechargeAccountList =
          rechargeOrderMapper.getSatisfyRechargeAccount(
              String.valueOf(memberWeal.getMinRechargeAmount()),
              formatter.format(memberWeal.getStartDate()),
              formatter.format(memberWeal.getEndDate()));
    } else {
      rechargeAccountList =
          memberSalaryInfoList.stream().map(MemberWealDetail::getUserName).collect(toList());
    }

    // 获取达到有效投注金额的会员账号
    List<String> betAccountList = new ArrayList<>();
    if (memberWeal.getMinBetAmount() != null
        && memberWeal.getMinBetAmount().compareTo(new BigDecimal("0")) > 0) {
      betAccountList =
          dayReportMapper.getSatisfyBetAccount(
              String.valueOf(memberWeal.getMinBetAmount()),
              formatter.format(memberWeal.getStartDate()),
              formatter.format(memberWeal.getEndDate()));
    } else {
      betAccountList =
          memberSalaryInfoList.stream().map(MemberWealDetail::getUserName).collect(toList());
    }

    // 取达到充值金额的会员账号&&达到有效投注金额的会员账号的交集
    List<String> intersectionList =
        rechargeAccountList.stream().filter(betAccountList::contains).collect(toList());
    if (type == 2) {
      // 生日礼金
      // 过滤每个会员的生日是否在周期内
      if (CollectionUtil.isNotEmpty(intersectionList)) {
        List<Member> users = memberMapper.findByUserNameList(intersectionList);
        intersectionList =
            users.stream()
                .filter(
                    item ->
                        item.getBirthday() != null
                            && DateUtil.parse(DateUtil.format(item.getBirthday(), "MM-dd"), "MM-dd")
                                    .compareTo(
                                        DateUtil.parse(
                                            DateUtil.format(memberWeal.getStartDate(), "MM-dd"),
                                            "MM-dd"))
                                >= 0
                            && DateUtil.parse(DateUtil.format(item.getBirthday(), "MM-dd"), "MM-dd")
                                    .compareTo(
                                        DateUtil.parse(
                                            DateUtil.format(memberWeal.getEndDate(), "MM-dd"),
                                            "MM-dd"))
                                <= 0)
                .map(Member::getAccount)
                .filter(intersectionList::contains)
                .collect(toList());
      }
    }

    // 取享有福利的会员账号，再次跟上面 intersectionList 集合账号取交集
    List<String> finalIntersectionList1 = intersectionList;
    List<MemberWealDetail> finalIntersectionList = new ArrayList<>();
    finalIntersectionList =
        memberSalaryInfoList.stream()
            .filter(
                item ->
                    finalIntersectionList1.stream()
                        .map(String::toString)
                        .collect(Collectors.toList())
                        .contains(item.getUserName()))
            .collect(Collectors.toList());

    List<MemberWealDetail> list = new ArrayList<>();

    if (CollectionUtil.isNotEmpty(finalIntersectionList)) {
      // 过滤黑名单
      List<String> blackList = new ArrayList<>();
      List<MemberBlackList> memberBlackList =
          blackListService.findMemberBlackList(new MemberBlackList());
      if (CollectionUtil.isNotEmpty(memberBlackList)) {
        blackList =
            memberBlackList.stream()
                .map(memberBlack -> memberBlack.getUserAccount())
                .collect(Collectors.toList());
      }
      if (CollectionUtil.isNotEmpty(blackList)) {
        for (String userAccount : blackList) {
          finalIntersectionList =
              finalIntersectionList.stream()
                  .filter(item1 -> !item1.getUserName().equalsIgnoreCase(userAccount))
                  .collect(Collectors.toList());
        }
      }
      // 装配福利详情等字段
      int totalUserCount = 0;
      BigDecimal totalPayMoney = new BigDecimal("0");
      for (MemberWealDetail memberWealDetail : finalIntersectionList) {
        MemberWealDetail model = new MemberWealDetail();
        model.setWealId(id);
        model.setUserId(memberWealDetail.getUserId());
        model.setUserName(memberService.getById(memberWealDetail.getUserId()).getAccount());
        model.setLevel(memberWealDetail.getLevel());
        model.setRewordAmount(memberWealDetail.getRewordAmount());
        model.setStatus(1);
        model.setCreateBy(GlobalContextHolder.getContext().getUsername());
        list.add(model);
        totalUserCount++;
        totalPayMoney = totalPayMoney.add(memberWealDetail.getRewordAmount());
      }
      memberWeal.setTotalUserCount(totalUserCount);
      memberWeal.setTotalPayMoney(totalPayMoney);
    }

    // fixme 无用代码及时删除
    List<MemberWealDetail> memberWealDetailList =
        wealDetailService.findSatisfyMember(
            new MemberWealDetail() {
              {
                setWealId(id);
              }
            });

    // 修改了福利信息时，要重新结算，所以应该先删除
    wealDetailService.removeWealDetail(id);
    if (CollectionUtil.isNotEmpty(list)) {
      wealDetailService.batchSave(list);
    }
    memberWeal.setSettleTime(new Date());
    memberWeal.setStatus(1);
    memberWeal.setUpdateBy(GlobalContextHolder.getContext().getUsername());
    memberWeal.setUpdateTime(new Date());
    if (!this.updateById(memberWeal)) {
      throw new ServiceException("结算失败!");
    }
  }

  /** 福利派发 */
  @Override
  public void distributeWeal(Long wealId, HttpServletRequest request) {
    // 按福利id去锁 防止重复派发
    String key = "weal:pay:" + wealId;
    RLock lock = redissonClient.getLock(key);
    if (lock.isLocked()) {
      return;
    }
    distributedLocker.lock(key);

    try {
      MemberWealDetail memberWealDetail = new MemberWealDetail();
      memberWealDetail.setWealId(wealId);
      // 获取本次福利需要派发的会员
      List<MemberWealDetail> list = wealDetailService.findSatisfyMember(memberWealDetail);
      String serialNumber = IdWorker.getIdStr();
      // 根据福利id查询该福利
      MemberWeal memberWeal = memberWealMapper.selectById(wealId);
      if (memberWeal == null) {
        throw new ServiceException("该福利不存在!");
      }
      if (CollectionUtil.isNotEmpty(list)) {
        // 过滤出已派发或已取消的
        list = list.stream().filter(item -> item.getStatus() == 1).collect(toList());
        // 1：周俸禄  2：月俸禄  3：生日礼金 4：每月红包
        Integer type = memberWeal.getType();
        // 查询成长值配置
        MemberGrowthConfig growthConfig =
            growthConfigMapper.findOneConfig(LanguageEnum.app_zh_CN.getCode());
        // 先计算出升级奖励的金额  可能会连升几级
        Integer limitLevel = growthConfig.getLimitLevel();
        if (limitLevel == null) {
          limitLevel = 50;
        }
        // 查询会员成长等级
        List<MemberGrowthLevel> levels =
            growthLevelMapper.findList(limitLevel + 1, LanguageEnum.app_zh_CN.getCode());
        // 每次7000条执行
        int pageSize = 7000;
        // 需要派发的会员记录数
        int size = list.size();
        log.info("需要派发的会员记录数:" + size);
        int part = size / pageSize;
        for (int j = 1; j <= part + 1; j++) {
          int fromIndex = (j - 1) * pageSize;
          int toIndex = j * pageSize;
          // 不需要分页
          if (size < toIndex) {
            toIndex = size;
          }
          log.info("当前fromIndex和toIndex：（{}），（{}）", fromIndex, toIndex);
          List<MemberWealDetail> pageList = list.subList(fromIndex, toIndex);
          // 开始处理
          if (CollectionUtil.isNotEmpty(pageList)) {
            log.info("福利{},第{}批开始执行,条数:{}", wealId, j, pageList.size());
            for (MemberWealDetail item : pageList) {
              try {
                if (item.getStatus() != 1) {
                  return;
                }
                int sourceType = 0;
                Integer level = item.getLevel();
                if (level >= levels.size()) {
                  level = levels.size() - 1;
                }
                String content = "";
                // 周俸禄
                if (WEEK_WEAL.match(type)) {
                  sourceType = WEEK_WEAL.getValue();
                  content = "本周俸禄奖励已派发 金额:" + item.getRewordAmount() + "，请领取";
                  // 月俸禄
                } else if (MONTH_WEAL.match(type)) {
                  sourceType = TranTypes.MONTH_WEAL.getValue();
                  content = "本月俸禄奖励已派发 金额:" + item.getRewordAmount() + "，请领取";
                  // 生日礼金
                } else if (BIRTH_WEAL.match(type)) {
                  sourceType = TranTypes.BIRTH_WEAL.getValue();
                  content = "您的生日礼金奖励已派发 金额:" + item.getRewordAmount() + "，请领取";
                  // 每月红包
                } else if (RED_ENVELOPE_WEAL.match(type)) {
                  sourceType = TranTypes.RED_ENVELOPE_WEAL.getValue();
                  content = "当月红包奖励已派发 金额:" + item.getRewordAmount() + "，请领取";
                }
                // 查询会员信息
                Member member = memberMapper.selectById(item.getUserId());
                if (BeanUtil.isEmpty(member)) {
                  return;
                }
                // 福利奖励
                MemberWealRewordAddDTO memberWealReword = new MemberWealRewordAddDTO();
                memberWealReword.setUserId(member.getId());
                memberWealReword.setUserName(member.getAccount());
                memberWealReword.setOldLevel(item.getLevel());
                memberWealReword.setCurrentLevel(item.getLevel());
                memberWealReword.setRewordAmount(item.getRewordAmount());
                memberWealReword.setUserType(member.getUserType());
                memberWealReword.setParentId(member.getParentId());
                memberWealReword.setParentName(member.getParentName());
                memberWealReword.setAgentPath(member.getSuperPath());
                // 0 升级奖励  1：周俸禄  2：月俸禄  3：生日礼金  4：每月红包
                memberWealReword.setType(type);
                memberWealReword.setSerialNumber(serialNumber);
                // 自动派发
                if (BooleanEnum.YES.match(growthConfig.getIsAutoPayReword())) {
                  // 将通知消息去掉请领取
                  content = content.replaceAll("，请领取", "");
                  String sourceId = String.valueOf(IdWorker.getId());
                  // 新增打码量记录
                  ValidWithdraw validWithdraw = new ValidWithdraw();
                  validWithdraw.setAccount(member.getAccount());
                  validWithdraw.setMemberId(member.getId());
                  validWithdraw.setRechId(sourceId);
                  validWithdraw.setRechMoney(
                      item.getRewordAmount().setScale(2, RoundingMode.HALF_UP));
                  validWithdraw.setCreateTime(new Date());
                  validWithdraw.setType(0);
                  validWithdraw.setStatus(0);
                  validWithdraw.setDiscountMoney(BigDecimal.ZERO);
                  validWithdraw.setRemark(member.getAccount() + content + "增加打码量");
                  validWithdrawService.saveValidWithdraw(validWithdraw);
                  // 账户资金锁
                  String lockKey =
                      MessageFormat.format(
                          MemberServiceKeyConstant.MEMBER_FINANCIAL_LOCK, member.getAccount());
                  try {
                    // 获取资金锁（等待6秒，租期120秒）
                    boolean flag = distributedLocker.tryLock(lockKey, TimeUnit.SECONDS, 6, 120);
                    // 6秒获取不到资金锁，进入下一次循环，下一个定时任务，再进行派彩
                    if (!flag) {
                      throw new ServiceException("操作失败");
                    }
                    // 添加流水记录
                    MemberBill memberBill = new MemberBill();
                    memberBill.setMemberId(member.getId());
                    memberBill.setAccount(member.getAccount());
                    memberBill.setMemberPath(member.getSuperPath());
                    memberBill.setTranType(sourceType);
                    memberBill.setOrderNo(sourceId);
                    memberBill.setAmount(item.getRewordAmount());
                    memberBill.setBalance(memberInfoService.getById(member.getId()).getBalance());
                    memberBill.setRemark(content);
                    memberBill.setContent(content);
                    memberBill.setOperator("system");
                    memberBillService.save(memberBill);

                    // 已完成
                    memberWealReword.setStatus(2);
                    // 默认当前时间为领取时间  实际上不需要领取
                    memberWealReword.setDrawTime(DateTime.now());
                  } catch (Exception e) {
                    log.error(
                        MessageFormat.format("会员{0}，VIP资金变动, 失败原因：{1}", member.getAccount(), e));
                    // 释放资金锁
                    distributedLocker.unlock(lockKey);
                  } finally {
                    // 释放资金锁
                    distributedLocker.unlock(lockKey);
                  }
                } else {
                  // 不自动派发则 需要领取
                  memberWealReword.setStatus(1);
                }
                // 6.5 通知 发个人消息
                MessageInfoAddDTO dto = new MessageInfoAddDTO();
                Message message = messageInfoConvert.toEntity(dto);

                message.setTitle("VIP福利奖励");
                message.setContent(content);
                message.setCategory(PushMessageEnum.MessageCategory.SYS_SEND.getValue());
                message.setPosition(PushMessageEnum.MessageCategory.CATE_DEF.getValue());
                message.setShowType(PushMessageEnum.MessageShowType.SHOW_DEF.value());
                message.setPopsCount(PushMessageEnum.PopCount.POP_COUNT_DEF.getValue());
                message.setPushRange(PushMessageEnum.UserRange.SOME_MEMBERS.getValue());
                message.setLinkAccount(member.getAccount());
                message.setType(1);
                message.setStatus(1);
                message.setCreateBy("System");
                messageMapper.saveReturnId(message);

                MessageDistribute messageDistribute = new MessageDistribute();
                messageDistribute.setMessageId(message.getId());
                messageDistribute.setUserId(member.getId());
                messageDistribute.setUserAccount(member.getAccount());
                messageDistribute.setRechargeLevel(member.getUserLevel());
                messageDistribute.setVipLevel(
                    memberInfoService.getById(member.getId()).getVipLevel());
                messageDistribute.setReadStatus(0);
                messageDistribute.setCreateBy("System");
                messageDistributeService.save(messageDistribute);
                // 添加奖励记录
                wealRewordService.insertMemberWealReword(memberWealReword);
                // 修改福利详情状态 为已完成
                wealDetailService.updateByWealStatus(item.getWealId(), 2);
              } catch (Exception e) {
                // fixme 异常不能吞掉
              }
            }
            // 本批次执行完 睡眠下
            //                        try {
            //                            log.info("开始分批睡眠执行派发");
            //                            Thread.sleep(10000);
            //                        } catch (InterruptedException e) {
            //                            log.info("睡眠时发生错误了!");
            //                            e.printStackTrace();
            //                        }
          }
        }
      }
      log.info("会员派发处理完毕!当前时间:{}", DateTime.now());
      // 修改福利表状态
      // 不管需不需要领取 福利和详情都应为 已完成 状态
      memberWeal.setStatus(2);
      memberWeal.setPayTime(DateTime.now());
      memberWeal.setSerialNumber(serialNumber);
      if (!this.updateById(memberWeal)) {
        throw new ServiceException("操作失败");
      }
      ;
      log.info("福利:{}派发完毕!", wealId);
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      distributedLocker.unlock(key);
    }
  }

  /** 福利回收 */
  @Override
  public void recycleWeal(Long wealId, HttpServletRequest request) {
    // 通过流水号去回收福利奖励-->未领取的直接回收掉但不扣钱  已完成的直接扣钱回收
    // 根据福利id查询该福利
    MemberWeal memberWeal = memberWealMapper.selectById(wealId);
    // 福利类型
    TranTypes tranType = this.getWealType(memberWeal.getType());
    // 流水号
    String serialNumber = memberWeal.getSerialNumber();
    List<MemberWealReword> rewordList =
        wealRewordService.findList(
            new MemberWealRewordDTO() {
              {
                setSerialNumber(serialNumber);
              }
            });
    // 登录ip
    String ipAddress = IPUtils.getIpAddress(request);
    if (CollectionUtil.isNotEmpty(rewordList)) {
      // 过滤已派发成功状态的数据, 每次7000条执行
      int pageSize = 7000;
      int size = rewordList.size();
      log.info("需要回收的会员记录数:" + size);

      int part = size / pageSize;
      for (int j = 1; j <= part + 1; j++) {
        int fromIndex = (j - 1) * pageSize;
        int toIndex = j * pageSize;
        if (size < toIndex) {
          // 不需要分页
          toIndex = size;
        }
        log.info("当前fromIndex和toIndex：（{}），（{}）", fromIndex, toIndex);
        List<MemberWealReword> pageList = rewordList.subList(fromIndex, toIndex);
        // 开始处理
        if (CollectionUtil.isNotEmpty(pageList)) {
          log.info("福利回收{},第{}批开始执行,条数:{}", wealId, j, pageList.size());
          for (MemberWealReword reword : pageList) {
            try {
              // 已完成
              if (reword.getStatus() == 2) {
                // 查询会员信息
                Member member = memberMapper.selectById(reword.getUserId());
                // 派发金额
                BigDecimal negate = reword.getRewordAmount().negate();
                // 账户资金锁
                String lockKey =
                    MessageFormat.format(
                        MemberServiceKeyConstant.MEMBER_FINANCIAL_LOCK, member.getAccount());
                try {
                  // 获取资金锁（等待8秒，租期120秒）
                  boolean flag = distributedLocker.tryLock(lockKey, TimeUnit.SECONDS, 8, 120);
                  if (!flag) {
                    return;
                  }
                  // 添加流水记录
                  MemberBill memberBill = new MemberBill();
                  memberBill.setMemberId(member.getId());
                  memberBill.setAccount(member.getAccount());
                  memberBill.setMemberPath(member.getSuperPath());
                  memberBill.setTranType(tranType.getValue());
                  memberBill.setOrderNo(RandomUtil.generateNumber(22));
                  memberBill.setAmount(negate.negate());
                  memberBill.setBalance(memberInfoService.getById(member.getId()).getBalance());
                  memberBill.setRemark(tranType.getDesc());
                  memberBill.setContent(tranType.getDesc());
                  memberBill.setOperator("system");
                  memberBillService.save(memberBill);
                } catch (Exception e) {
                  log.error(
                      MessageFormat.format("会员：{0}，VIP资金变动, 失败原因：{2}", member.getAccount(), e));
                  // 释放资金锁
                  distributedLocker.unlock(lockKey);
                } finally {
                  // 释放资金锁
                  distributedLocker.unlock(lockKey);
                }
              }
              // 已失效状态
              reword.setStatus(3);
              // 失效时间
              reword.setInvalidTime(DateTime.now());
              // 修改福利奖励
              wealRewordService.updateWealRecord(reword);
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
          // 本批次执行完 睡眠下
          //                    try {
          //                        log.info("开始分批睡眠执行派发");
          //                        //睡眠5s
          //                        Thread.sleep(5000);
          //                    } catch (InterruptedException e) {
          //                        log.info("睡眠时发生错误了!");
          //                        e.printStackTrace();
          //                    }
        }
      }
    }
    log.info("福利回收处理完毕!当前时间:{}", DateTime.now());

    // 修改福利详情(状态)
    wealDetailService.updateByWealStatus(memberWeal.getId(), 3);

    // 修改福利发放
    memberWeal.setStatus(3);
    if (!this.updateById(memberWeal)) {
      throw new ServiceException("操作失败");
    }
  }

  private TranTypes getWealType(Integer type) {
    if (MONTH_WEAL_RECYCLE.match(type)) {
      return MONTH_WEAL_RECYCLE;
    }

    if (BIRTH_WEAL_RECYCLE.match(type)) {
      return BIRTH_WEAL_RECYCLE;
    }

    if (RED_ENVELOPE_WEAL_RECYCLE.match(type)) {
      return RED_ENVELOPE_WEAL_RECYCLE;
    }

    return WEEK_WEAL_RECYCLE;
  }
}
