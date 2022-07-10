package com.gameplat.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.constant.BuildInDiscountType;
import com.gameplat.admin.constant.RechargeMode;
import com.gameplat.admin.constant.TrueFalse;
import com.gameplat.admin.convert.RechargeOrderConvert;
import com.gameplat.admin.enums.BlacklistConstant.BizBlacklistType;
import com.gameplat.admin.feign.MessageFeignClient;
import com.gameplat.admin.mapper.MessageMapper;
import com.gameplat.admin.mapper.RechargeOrderHistoryMapper;
import com.gameplat.admin.mapper.RechargeOrderMapper;
import com.gameplat.admin.model.bean.*;
import com.gameplat.admin.model.dto.*;
import com.gameplat.admin.model.vo.*;
import com.gameplat.admin.service.*;
import com.gameplat.admin.util.MoneyUtils;
import com.gameplat.base.common.enums.EnableEnum;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.json.JsonUtils;
import com.gameplat.base.common.snowflake.IdGeneratorSnowflake;
import com.gameplat.base.common.util.DateUtil;
import com.gameplat.common.constant.SocketEnum;
import com.gameplat.common.enums.*;
import com.gameplat.common.lang.Assert;
import com.gameplat.common.model.bean.UserEquipment;
import com.gameplat.common.model.bean.limit.MemberRechargeLimit;
import com.gameplat.common.util.CNYUtils;
import com.gameplat.message.model.MessagePayload;
import com.gameplat.model.entity.DiscountType;
import com.gameplat.model.entity.member.Member;
import com.gameplat.model.entity.member.MemberBill;
import com.gameplat.model.entity.member.MemberGrowthConfig;
import com.gameplat.model.entity.member.MemberInfo;
import com.gameplat.model.entity.message.Message;
import com.gameplat.model.entity.pay.PayAccount;
import com.gameplat.model.entity.pay.TpMerchant;
import com.gameplat.model.entity.pay.TpPayChannel;
import com.gameplat.model.entity.recharge.RechargeOrder;
import com.gameplat.model.entity.recharge.RechargeOrderHistory;
import com.gameplat.model.entity.spread.SpreadUnion;
import com.gameplat.model.entity.sys.SysDictData;
import com.gameplat.model.entity.sys.SysUser;
import com.gameplat.security.SecurityUserHolder;
import com.gameplat.security.context.UserCredential;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

import static com.gameplat.common.enums.DictDataEnum.MAX_DISCOUNT_MONEY;
import static com.gameplat.common.enums.DictDataEnum.MAX_RECHARGE_MONEY;

@Slf4j
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class RechargeOrderServiceImpl extends ServiceImpl<RechargeOrderMapper, RechargeOrder>
    implements RechargeOrderService {

  @Autowired private RechargeOrderConvert rechargeOrderConvert;

  @Autowired(required = false)
  private RechargeOrderMapper rechargeOrderMapper;

  @Autowired(required = false)
  private RechargeOrderHistoryMapper rechargeOrderHistoryMapper;

  @Autowired private LimitInfoService limitInfoService;

  @Autowired private MemberService memberService;

  @Autowired private SysUserService sysUserService;

  @Autowired private MemberInfoService memberInfoService;

  @Autowired private PayAccountService payAccountService;

  @Autowired private TpMerchantService tpMerchantService;

  @Autowired private TpPayChannelService tpPayChannelService;

  @Autowired private ConfigService configService;

  @Autowired private BizBlacklistFacade bizBlacklistFacade;

  @Autowired private DiscountTypeService discountTypeService;

  @Autowired private MemberBillService memberBillService;

  @Autowired private ValidWithdrawService validWithdrawService;

  @Autowired private MemberRwReportService memberRwReportService;

  @Autowired private MemberGrowthConfigService memberGrowthConfigService;

  @Autowired @Lazy private MemberGrowthStatisService memberGrowthStatisService;

  @Autowired(required = false)
  private MessageMapper messageMapper;

  @Autowired private MessageDistributeService messageDistributeService;

  @Autowired private SysDictDataService sysDictDataService;

  @Autowired(required = false)
  private MessageFeignClient client;

  @Override
  public PageExt<RechargeOrderVO, SummaryVO> findPage(
      Page<RechargeOrder> page, RechargeOrderQueryDTO dto) {
    LambdaQueryWrapper<RechargeOrder> query = Wrappers.lambdaQuery();
    query
        .in(ObjectUtils.isNotNull(dto.getModeList()), RechargeOrder::getMode, dto.getModeList())
        .in(
            ObjectUtils.isNotNull(dto.getStatusList()),
            RechargeOrder::getStatus,
            dto.getStatusList())
        .eq(
            ObjectUtils.isNotEmpty(dto.getPointFlag()),
            RechargeOrder::getPointFlag,
            dto.getPointFlag())
        .in(
            ObjectUtils.isNotNull(dto.getPayAccountList()),
            RechargeOrder::getPayAccountAccount,
            dto.getPayAccountList())
        .eq(
            ObjectUtils.isNotEmpty(dto.getTpMerchantId()),
            RechargeOrder::getTpMerchantId,
            dto.getTpMerchantId())
        .ge(
            ObjectUtils.isNotEmpty(dto.getAmountFrom()),
            RechargeOrder::getAmount,
            dto.getAmountFrom())
        .le(ObjectUtils.isNotEmpty(dto.getAmountTo()), RechargeOrder::getAmount, dto.getAmountTo())
        .eq(ObjectUtils.isNotEmpty(dto.getAccount()), RechargeOrder::getAccount, dto.getAccount())
        .in(
            ObjectUtils.isNotEmpty(dto.getMemberType())
                && MemberEnums.Type.MEMBER.match(dto.getMemberType()),
            RechargeOrder::getMemberType,
            MemberEnums.Type.MEMBER.value(),
                MemberEnums.Type.AGENT.value())
        .eq(
            ObjectUtils.isNotEmpty(dto.getMemberType())
                && MemberEnums.Type.PROMOTION.match(dto.getMemberType()),
            RechargeOrder::getMemberType,
            dto.getMemberType())
        .eq(ObjectUtils.isNotEmpty(dto.getOrderNo()), RechargeOrder::getOrderNo, dto.getOrderNo())
        .eq(
            ObjectUtils.isNotEmpty(dto.getSuperAccount()),
            RechargeOrder::getSuperAccount,
            dto.getSuperAccount())
        .ge(
            ObjectUtils.isNotEmpty(dto.getCreateTimeFrom()),
            RechargeOrder::getCreateTime,
            dto.getCreateTimeFrom())
        .le(
            ObjectUtils.isNotEmpty(dto.getCreateTimeTo()),
            RechargeOrder::getCreateTime,
            dto.getCreateTimeTo())
        .in(
            ObjectUtils.isNotNull(dto.getMemberLevelList()),
            RechargeOrder::getMemberLevel,
            dto.getMemberLevelList());
    if (dto.isFullNameFuzzy()) {
      query.like(
          ObjectUtils.isNotEmpty(dto.getFullName()), RechargeOrder::getNickname, dto.getFullName());
    } else {
      query.eq(
          ObjectUtils.isNotEmpty(dto.getFullName()), RechargeOrder::getNickname, dto.getFullName());
    }
    query.orderBy(
        ObjectUtils.isNotEmpty(dto.getOrder()),
        !ObjectUtils.isEmpty(dto.getOrder()) && dto.getOrder().equals("ASC"),
        RechargeOrder::getCreateTime);
    IPage<RechargeOrderVO> data = this.page(page, query).convert(rechargeOrderConvert::toVo);
    // 统计受理充值订单总金额、未受理充值订单总金额
    SummaryVO summaryVO = amountSum(dto);
    return new PageExt<RechargeOrderVO, SummaryVO>(data, summaryVO);
  }

  @Override
  public void updateDiscount(
      Long id, Integer discountType, BigDecimal discountAmount, BigDecimal discountDml) {
    Integer discountRechargeFlag = 1;
    if (null == discountType) {
      discountAmount = BigDecimal.ZERO;
      discountDml = BigDecimal.ZERO;
      discountRechargeFlag = null;
    }
    UserCredential userCredential = SecurityUserHolder.getCredential();
    RechargeOrder rechargeOrder = this.getById(id);
    rechargeOrder.setAuditorAccount(userCredential.getUsername());
    rechargeOrder.setAuditTime(new Date());
    rechargeOrder.setDiscountType(discountType);
    rechargeOrder.setDiscountAmount(discountAmount);
    rechargeOrder.setDiscountDml(discountDml);
    rechargeOrder.setTotalAmount(rechargeOrder.getPayAmount().add(discountAmount));
    rechargeOrder.setDiscountRechargeFlag(discountRechargeFlag);
    this.updateById(rechargeOrder);
  }

  @Override
  public void updateRemarks(Long id, String auditRemarks) {
    if (null == auditRemarks) {
      throw new ServiceException("备注信息不能为空!");
    }
    UserCredential userCredential = SecurityUserHolder.getCredential();
    LambdaUpdateWrapper<RechargeOrder> update = Wrappers.lambdaUpdate();
    update
        .set(RechargeOrder::getAuditRemarks, auditRemarks)
        .set(RechargeOrder::getAuditorAccount, userCredential.getUsername())
        .set(RechargeOrder::getAuditTime, new Date())
        .eq(RechargeOrder::getId, id);
    this.update(new RechargeOrder(), update);
  }

  @Override
  public void handle(Long id) {
    this.updateStatus(id, RechargeStatus.UNHANDLED.getValue(), RechargeStatus.HANDLED.getValue());
  }

  @Override
  public void unHandle(Long id) {
    RechargeOrder rechargeOrder = this.getById(id);
    // 校验已处理订单是否允许其他账户操作
    crossAccountCheck(rechargeOrder);
    // 更新充值订单状态
    this.updateStatus(id, RechargeStatus.HANDLED.getValue(), RechargeStatus.UNHANDLED.getValue());
  }

  @Override
  public void accept(Long id, String auditRemarks, boolean flag) {
    RechargeOrder rechargeOrder = this.getById(id);
    // 系统最高配置金额（充值金额  优惠金额）
    if (!flag) {
      String maxAmount = configService.getValue(MAX_RECHARGE_MONEY);
      if (rechargeOrder.getAmount().compareTo(new BigDecimal(maxAmount)) > 0) {
        throw new ServiceException("充值金额不能大于系统配置的最访问被拒绝，您没有权限访问该资源高金额：" + maxAmount);
      }

      String maxDiscount = configService.getValue(MAX_DISCOUNT_MONEY);
      if (rechargeOrder.getDiscountAmount() != null) {
        if (rechargeOrder.getDiscountAmount().compareTo(new BigDecimal(maxDiscount)) > 0) {
          throw new ServiceException("充值优惠金额不能大于系统配置的最高金额：" + maxDiscount);
        }
      }
    }
    // 校验订单状态
    verifyRechargeOrderForAuditing(rechargeOrder);
    // 校验已处理订单是否允许其他账户操作
    crossAccountCheck(rechargeOrder);
    // 校验审核流程
    if (flag) {
      rechargeProcess(rechargeOrder);
    }
    // 校验会员账户状态
    Member member = memberService.getById(rechargeOrder.getMemberId());
    MemberInfo memberInfo = memberInfoService.getById(rechargeOrder.getMemberId());
    verifyUser(member, memberInfo, rechargeOrder.getMode() == RechargeMode.TRANSFER.getValue());

    // 校验子账号当日存款审核额度
    SysUser sysUser = sysUserService.getByUsername(SecurityUserHolder.getUsername());
    AdminLimitInfo adminLimitInfo = JsonUtils.parse(sysUser.getLimitInfo(), AdminLimitInfo.class);
    if (null != sysUser && StringUtils.equals(UserTypes.SUBUSER.value(), sysUser.getUserType())) {
      checkZzhRechargeAmountAudit(
          sysUser.getUserName(),
          adminLimitInfo,
          rechargeOrder.getMode(),
          rechargeOrder.getTotalAmount());
    }

    // 重新校验首充优惠
    recalculateDiscountAmount(rechargeOrder, memberInfo.getTotalRechTimes() > 0);

    // 更新订单状态
    rechargeOrder.setAuditorAccount(sysUser.getUserName());
    rechargeOrder.setAuditTime(new Date());
    if (null != auditRemarks) {
      rechargeOrder.setAuditRemarks(auditRemarks);
    }
    rechargeOrder.setStatus(RechargeStatus.SUCCESS.getValue());
    updateRechargeOrder(rechargeOrder);
    // 更新充提报表
    // 过滤 推广账号 充值
    if (!UserTypes.PROMOTION.value().equals(member.getUserType())) {
      memberRwReportService.addRecharge(member, memberInfo.getTotalRechTimes(), rechargeOrder);
    }

    if (rechargeOrder.getDmlFlag() == TrueFalse.TRUE.getValue()) {
      // 计算打码量
      validWithdrawService.addRechargeOrder(rechargeOrder);
    }
    // 添加会员账变
    addUserBill(rechargeOrder, member, memberInfo.getBalance(), sysUser.getUserName());
    // 更新充值金额累积
    updateRechargeMoney(rechargeOrder, member.getUserType());

    // 更新会员充值信息
    memberInfoService.updateBalanceWithRecharge(
            memberInfo.getMemberId(),
            rechargeOrder.getPayAmount(),
            rechargeOrder.getTotalAmount(),
            rechargeOrder.getPointFlag());
    // 判断充值是否计算积分
    if (TrueFalse.TRUE.getValue() != rechargeOrder.getPointFlag()) {
      log.info(
          "充值不增加成长值 account={}，orderNo={},pointFlag={}",
          rechargeOrder.getAccount(),
          rechargeOrder.getOrderNo(),
          rechargeOrder.getPointFlag());
      return;
    }

    // 兑换成长值
    MemberGrowthConfig memberGrowthConfig = memberGrowthConfigService.getOneConfig();
    if (memberGrowthConfig.getIsEnableVip() == TrueFalse.FALSE.getValue()) {
      log.info(
          "会员充值兑换成长值失败-未开启VIP功能开关: account={}，orderNo={},pointFlag={}",
          rechargeOrder.getAccount(),
          rechargeOrder.getOrderNo(),
          rechargeOrder.getPointFlag());
      return;
    }
    if (memberGrowthConfig.getIsEnableRecharge() == TrueFalse.FALSE.getValue()) {
      log.info(
          "会员充值兑换成长值失败-未开启充值成长计算开关： account={}，orderNo={},pointFlag={}",
          rechargeOrder.getAccount(),
          rechargeOrder.getOrderNo(),
          rechargeOrder.getPointFlag());
      return;
    }
    MemberGrowthChangeDto memberGrowthChangeDto = new MemberGrowthChangeDto();
    memberGrowthChangeDto.setUserId(rechargeOrder.getMemberId());
    memberGrowthChangeDto.setUserName(rechargeOrder.getAccount());
    memberGrowthChangeDto.setType(GrowthChangeEnum.recharge.getCode());
    memberGrowthChangeDto.setChangeGrowth(
        memberGrowthConfig.getRechageRate().multiply(rechargeOrder.getAmount()).longValue());
    memberGrowthChangeDto.setRemark("人工充值成长值变动");

    memberGrowthStatisService.changeGrowth(memberGrowthChangeDto);

    // 入款成功 添加 消息  mode   在线 转账支付
    if (ObjectUtil.equals(RechargeMode.TRANSFER.getValue(), rechargeOrder.getMode())
        || ObjectUtil.equals(RechargeMode.ONLINE_PAY.getValue(), rechargeOrder.getMode())) {
      this.addMessageInfo(rechargeOrder, RechargeStatus.SUCCESS.getValue());
      MemberRechargeLimit limit = limitInfoService.getRechargeLimit();
      this.sendMessage(
          rechargeOrder.getAccount(), SocketEnum.SOCKET_RECHARGE_SUCCESS, limit.getRechargeTip());
    }
  }

  @Override
  public void cancel(Long id) throws ServiceException {
    RechargeOrder rechargeOrder = this.getById(id);
    // 校验订单状态
    verifyRechargeOrderForAuditing(rechargeOrder);
    // 校验已处理订单是否允许其他账户操作
    crossAccountCheck(rechargeOrder);
    // 更新订单状态
    rechargeOrder.setAuditorAccount(SecurityUserHolder.getUsername());
    rechargeOrder.setAuditTime(new Date());
    rechargeOrder.setStatus(RechargeStatus.CANCELLED.getValue());
    updateRechargeOrder(rechargeOrder);
    // 添加消息
    this.addMessageInfo(rechargeOrder, RechargeStatus.CANCELLED.getValue());
    // 消息推送到 socket
    this.sendMessage(
        rechargeOrder.getAccount(),
        SocketEnum.SOCKET_RECHARGE_FAIL,
        SocketEnum.SEND_RECHARGE_FAIL_MESSAGE);
  }

  @Override
  public void updateStatus(Long id, Integer curStatus, Integer newStatus) {
    String auditorAccount = SecurityUserHolder.getUsername();
    LambdaUpdateWrapper<RechargeOrder> update = Wrappers.lambdaUpdate();
    update
        .set(RechargeOrder::getStatus, newStatus)
        .set(RechargeOrder::getAcceptAccount, auditorAccount)
        .set(RechargeOrder::getAcceptTime, new Date())
        .set(RechargeOrder::getAuditorAccount, auditorAccount)
        .set(RechargeOrder::getAuditTime, new Date())
        .eq(RechargeOrder::getId, id)
        .eq(RechargeOrder::getStatus, curStatus);

    if (!this.update(new RechargeOrder(), update)) {
      String msg = String.format("充值订单状态已变化或无效的参数[%s]请刷新重试.", id);
      log.error(msg);
      throw new ServiceException(msg);
    }
  }

  @Override
  public void manual(ManualRechargeOrderBo manualRechargeOrderBo, UserEquipment userEquipment) {
    RechargeOrder rechargeOrder = buildManualRechargeOrder(manualRechargeOrderBo);
    // 填充客户端信息
    fillClientInfo(rechargeOrder, userEquipment);
    this.save(rechargeOrder);

    if (manualRechargeOrderBo.isSkipAuditing()) {
      String auditRemarks =
          StringUtils.isBlank(manualRechargeOrderBo.getAuditRemarks()) ? null : "直接入款";
      accept(rechargeOrder.getId(), auditRemarks, false);
    }
  }

  @Override
  public List<ActivityStatisticItem> findRechargeDateList(Map<String, Object> map) {
    List<ActivityStatisticItem> rechargeDateList = rechargeOrderMapper.findRechargeDateList(map);
    if (CollectionUtils.isNotEmpty(rechargeDateList)) {
      // 将逗号分隔的日期String转成List<Date>R
      for (ActivityStatisticItem rechargeDate : rechargeDateList) {
        if (StringUtils.isNotEmpty(rechargeDate.getRechargeTimes())) {
          List<String> dateList = Arrays.asList(rechargeDate.getRechargeTimes().split(","));
          List<String> list = dateList.stream().distinct().sorted().collect(Collectors.toList());
          List<Date> dates = new ArrayList<>();
          for (String date : list) {
            dates.add(DateUtil.strToDate(date, DateUtil.YYYY_MM_DD));
          }
          rechargeDate.setRechargeTimeList(dates);
        }
      }
    }
    return rechargeDateList;
  }

  @Override
  public List<ActivityStatisticItem> findAllFirstRechargeAmount(Map<String, Object> map) {
    String startTime = (String) map.get("startTime");
    String endTime = (String) map.get("endTime");
    // 查询出包含历史记录在内会员的首次成功充值订单
    map.remove("startTime");
    map.remove("endTime");
    List<ActivityStatisticItem> firstRechargeOrderList =
        rechargeOrderMapper.findFirstRechargeOrderList(map);
    if (CollectionUtils.isNotEmpty(firstRechargeOrderList)) {
      for (ActivityStatisticItem firstRechargeOrder : firstRechargeOrderList) {
        // 判断该订单的充值时间是否在活动的有效期内
        if (cn.hutool.core.date.DateUtil.isIn(
            firstRechargeOrder.getRechargeTime(),
            cn.hutool.core.date.DateUtil.parseDate(startTime),
            cn.hutool.core.date.DateUtil.parseDate(endTime))) {
          firstRechargeOrder.setIsNewUser(1);
        } else {
          firstRechargeOrder.setIsNewUser(2);
        }
      }
      return firstRechargeOrderList;
    }
    return null;
  }

  /** 根据会员和最后修改时间获取充值次数、充值金额、充值优惠、其它优惠 */
  @Override
  public MemberActivationVO getRechargeInfoByNameAndUpdateTime(
      MemberActivationDTO memberActivationDTO) {
    return rechargeOrderMapper.getRechargeInfoByNameAndUpdateTime(memberActivationDTO);
  }

  private void addMessageInfo(RechargeOrder rechargeOrder, Integer state) {
    if (this.verifyMessage() == com.gameplat.common.enums.TrueFalse.FALSE.getValue()) {
      return;
    }
    Message messageInfo = new Message();
    messageInfo.setTitle(title(1, state));
    messageInfo.setContent(
        context(state, rechargeOrder.getCreateTime(), rechargeOrder.getAmount(), 1));
    messageInfo.setCategory(4);
    messageInfo.setPosition(1);
    messageInfo.setShowType(0);
    messageInfo.setPopsCount(0);
    messageInfo.setPushRange(2);
    messageInfo.setLinkAccount(rechargeOrder.getAccount());
    messageInfo.setSort(0);
    messageInfo.setType(1);
    messageInfo.setLanguage("zh-CN");
    messageInfo.setStatus(1);
    messageInfo.setImmediateFlag(0);
    messageInfo.setRemarks("系统消息");
    messageInfo.setCreateBy("system");
    messageInfo.setCreateTime(new Date());
    messageMapper.insert(messageInfo);

//    MessageDistribute messageDistribute = new MessageDistribute();
//    messageDistribute.setMessageId(messageInfo.getId());
//    messageDistribute.setUserId(rechargeOrder.getMemberId());
//    messageDistribute.setUserAccount(messageInfo.getLinkAccount());
//    messageDistribute.setRechargeLevel(Convert.toInt(rechargeOrder.getMemberLevel()));
//    messageDistribute.setVipLevel(
//        memberInfoService
//            .lambdaQuery()
//            .eq(MemberInfo::getMemberId, rechargeOrder.getMemberId())
//            .one()
//            .getVipLevel());
//    messageDistribute.setReadStatus(NumberConstant.ZERO);
//    messageDistribute.setCreateBy("system");
//    messageDistributeService.save(messageDistribute);
  }

  private void sendMessage(String account, String channel, String message) {
    MessagePayload messagePayload = MessagePayload.builder().channel(channel).title(message).build();
    log.info("充值成功=============>开始推送Socket消息,相关参数{}", messagePayload);
    client.sendToUser(account, messagePayload);
  }

  private int verifyMessage() {
    SysDictData sysDictData =
        sysDictDataService.getDictData(
            DictTypeEnum.SYSTEM_PARAMETER_CONFIG.getValue(),
            DictDataEnum.RECHARGE_PUSH_MSG.getLabel());
    return ObjectUtil.isNull(sysDictData) ? 0 : Convert.toInt(sysDictData.getDictValue());
  }

  /**
   * @param state 状态
   * @param date 时间
   * @param money 金额
   * @param mode 1 充值 2 提现
   * @return String
   */
  private String context(Integer state, Date date, BigDecimal money, int mode) {
    String context = "";
    String dateStr = DateUtil.dateToStr(date, DateUtil.YYYY_MM_DD_HH_MM_SS);

    if (ObjectUtil.equals(1, mode)) {
      if (ObjectUtil.equals(3, state)) {
        context = "您于" + dateStr + "充值的" + money.setScale(2, RoundingMode.HALF_UP) + "已成功到账。";
      } else {
        context = "您于" + dateStr + "充值的" + money.setScale(2, RoundingMode.HALF_UP) + "已取消。";
      }
    }

    return context;
  }

  /**
   * @param mode 1 充值 2 提现
   * @return String
   */
  private String title(int mode, int state) {
    String title = "";
    if (ObjectUtil.equals(1, mode)) {
      if (ObjectUtil.equals(state, 3)) {
        title = "充值成功";
      } else {
        title = "充值失败";
      }
    }
    return title;
  }

  private void verifyRechargeOrderForAuditing(RechargeOrder rechargeOrder) {
    if (rechargeOrder == null) {
      throw new ServiceException("无效的充值订单。");
    }
    if (rechargeOrder.getAmount().compareTo(BigDecimal.ZERO) < 0
        || rechargeOrder.getTotalAmount().compareTo(BigDecimal.ZERO) < 0) {
      throw new ServiceException("无效的充值金额。");
    }
    if (rechargeOrder.getStatus() != RechargeStatus.UNHANDLED.getValue()
        && rechargeOrder.getStatus() != RechargeStatus.HANDLED.getValue()) {
      throw new ServiceException("订单已处理。");
    }
  }

  /** 开启出入款订单是否允许其他账户操作配置 校验非超管账号是否原受理人 校验订单状态 */
  public void crossAccountCheck(RechargeOrder rechargeOrder) {
    UserCredential userCredential = SecurityUserHolder.getCredential();
    MemberRechargeLimit limit = limitInfoService.getRechargeLimit();
    boolean toCheck =
        BooleanEnum.NO.match(limit.getIsHandledAllowOthersOperate())
            && !userCredential.isSuperAdmin();
    if (toCheck) {
      if (!Objects.equals(RechargeStatus.UNHANDLED.getValue(), rechargeOrder.getStatus())
              && ObjectUtil.isNotEmpty(rechargeOrder.getAcceptAccount())
              && !userCredential.getUsername().equals(rechargeOrder.getAcceptAccount())) {
        throw new ServiceException("您无权操作此订单:" + rechargeOrder.getOrderNo());
      }
    }
  }

  public void rechargeProcess(RechargeOrder rechargeOrder) {
    MemberRechargeLimit limit = limitInfoService.getRechargeLimit();
    boolean isRechargeProcess = BooleanEnum.YES.match(limit.getIsRechargeProcess());
    if (isRechargeProcess
        && !ObjectUtil.equal(RechargeStatus.HANDLED.getValue(), rechargeOrder.getStatus())) {
      throw new ServiceException("请先受理订单:" + rechargeOrder.getOrderNo());
    }
  }

  private void verifyUser(Member member, MemberInfo memberInfo, boolean checkLimitRech) {
    if (member == null) {
      throw new ServiceException("memberInfo_exist");
    }
    if (memberInfo == null) {
      throw new ServiceException("user_not_exist");
    }
    if (!MemberEnums.Status.ENABlED.match(member.getStatus())) {
      log.error(
          "--account:{},userId:{},state:{}--",
          member.getAccount(),
          member.getId(),
          member.getStatus());
      throw new ServiceException(String.format("会员账号已冻结:%s", member.getAccount()));
    }
    if (MemberEnums.Type.TEST.match(member.getUserType())) {
      throw new ServiceException("试玩会员无法充值。");
    }
  }

  private void checkZzhRechargeAmountAudit(
      String userName, AdminLimitInfo adminLimitInfo, Integer mode, BigDecimal rechargeAmount) {

    if (mode == RechargeMode.TRANSFER.getValue() || mode == RechargeMode.ONLINE_PAY.getValue()) {

      if (!adminLimitInfo.getMaxRechargeAmount().equals(BigDecimal.ZERO)
          && rechargeAmount.compareTo(adminLimitInfo.getMaxRechargeAmount()) > 0) {
        StringBuffer buffer =
            new StringBuffer(userName)
                .append("单笔入款受限。入款额度为：")
                .append(adminLimitInfo.getMaxRechargeAmount())
                .append("元。 超过额度")
                .append(
                    MoneyUtils.toYuanStr(
                        rechargeAmount.subtract(adminLimitInfo.getMaxRechargeAmount())))
                .append("元");
        throw new ServiceException(buffer.toString());
      }
    } else if (mode == RechargeMode.MANUAL.getValue()) {
      if (!adminLimitInfo.getMaxManualRechargeAmount().equals(BigDecimal.ZERO)
          && rechargeAmount.compareTo(adminLimitInfo.getMaxManualRechargeAmount()) > 0) {
        StringBuffer buffer =
            new StringBuffer(userName)
                .append("单笔人工入款受限。人工入款额度为：")
                .append(adminLimitInfo.getMaxManualRechargeAmount())
                .append("元。  超过额度")
                .append(
                    MoneyUtils.toYuanStr(
                        rechargeAmount.subtract(adminLimitInfo.getMaxManualRechargeAmount())))
                .append("元");
        throw new ServiceException(buffer.toString());
      }
    }
  }

  private void recalculateDiscountAmount(RechargeOrder rechargeOrder, boolean isFirstRecharge) {
    if (Objects.equals(rechargeOrder.getMode(), RechargeMode.MANUAL.getValue())) {
      return;
    }
    if (BigDecimal.ZERO.compareTo(rechargeOrder.getDiscountAmount()) > 0) {
      return;
    }
    if (!Objects.equals(
        rechargeOrder.getDiscountRechargeFlag(),
        BuildInDiscountType.FIRST_RECHARGE.getRechargeFlag())) {
      return;
    }
    if (!Objects.equals(
        rechargeOrder.getDiscountType(), BuildInDiscountType.FIRST_RECHARGE.getValue())) {
      return;
    }
    if (!isFirstRecharge) {
      return;
    }
    rechargeOrder.setDiscountAmount(BigDecimal.ZERO);
    rechargeOrder.setDiscountDml(BigDecimal.ZERO);
    rechargeOrder.setDiscountRechargeFlag(0);
    rechargeOrder.setDiscountType(null);
    rechargeOrder.setTotalAmount(rechargeOrder.getAmount());
  }

  private void updateRechargeOrder(RechargeOrder rechargeOrder) {
    LambdaUpdateWrapper<RechargeOrder> update = Wrappers.lambdaUpdate();
    update
        .set(RechargeOrder::getStatus, rechargeOrder.getStatus())
        .set(RechargeOrder::getAuditTime, rechargeOrder.getAuditTime())
        .set(RechargeOrder::getAuditorAccount, rechargeOrder.getAuditorAccount())
        .eq(RechargeOrder::getId, rechargeOrder.getId());
    if (!this.update(new RechargeOrder(), update)) {
      throw new ServiceException("订单状态异常");
    }
    RechargeOrderHistory rechargeOrderHistory = new RechargeOrderHistory();
    BeanUtils.copyProperties(rechargeOrder, rechargeOrderHistory);
    rechargeOrderHistoryMapper.insert(rechargeOrderHistory);
  }

  private void updateRechargeMoney(RechargeOrder rechargeOrder, String userType) {
    // 收款账号通道金额更新
    if (rechargeOrder.getPayAccountId() != null) {
      PayAccount payAccount = payAccountService.getById(rechargeOrder.getPayAccountId());
      if (payAccount == null) {
        throw new ServiceException("请确认收款账号是否被删除?操作日志中可查询");
      }
      if (!StringUtils.equals(UserTypes.PROMOTION.value(), userType)) {
        payAccountService
            .lambdaUpdate()
            .set(PayAccount::getRechargeTimes, payAccount.getRechargeTimes() + 1)
            .set(
                PayAccount::getRechargeAmount,
                payAccount.getRechargeAmount().add(rechargeOrder.getPayAmount()))
            .eq(PayAccount::getId, payAccount.getId())
            .update(new PayAccount());
      }

      // 校验收款通道充值总金额
      if (validateLimitAmount(payAccount.getLimitInfo(), payAccount.getRechargeAmount())) {
        LambdaUpdateWrapper<PayAccount> update = Wrappers.lambdaUpdate();
        update
            .set(PayAccount::getStatus, SwitchStatusEnum.DISABLED.getValue())
            .eq(PayAccount::getId, payAccount.getId());
        payAccountService.update(new PayAccount(), update);
      }
    }

    // 在线商户充值金额更新
    if (rechargeOrder.getTpMerchantId() != null) {
      TpMerchant tpMerchant = tpMerchantService.getById(rechargeOrder.getTpMerchantId());
      if (tpMerchant == null) {
        throw new ServiceException("请确认商户是否被删除?操作日志中可查询");
      }
      LambdaUpdateWrapper<TpMerchant> update = Wrappers.lambdaUpdate();
      update
          .set(TpMerchant::getRechargeTimes, tpMerchant.getRechargeTimes() + 1)
          .set(
              TpMerchant::getRechargeAmount,
              tpMerchant.getRechargeAmount().add(rechargeOrder.getPayAmount()))
          .eq(TpMerchant::getId, tpMerchant.getId());
      tpMerchantService.update(new TpMerchant(), update);
    }

    // 在线通道充值总金额更新
    if (rechargeOrder.getTpPayChannelId() != null) {
      TpPayChannel tpPayChannel = tpPayChannelService.getById(rechargeOrder.getTpPayChannelId());
      if (tpPayChannel == null) {
        throw new ServiceException("请确认在线通道是否被删除?操作日志中可查询");
      }
      if (!StringUtils.equals(UserTypes.PROMOTION.value(), userType)) {
        LambdaUpdateWrapper<TpPayChannel> update = Wrappers.lambdaUpdate();
        update
            .set(TpPayChannel::getRechargeTimes, tpPayChannel.getRechargeTimes() + 1)
            .set(
                TpPayChannel::getRechargeAmount,
                tpPayChannel.getRechargeAmount().add(rechargeOrder.getPayAmount()))
            .eq(TpPayChannel::getId, tpPayChannel.getId());
        tpPayChannelService.update(update);
      }

      // 校验在线通道充值总金额
      if (validateLimitAmount(tpPayChannel.getLimitInfo(), tpPayChannel.getRechargeAmount())) {
        LambdaUpdateWrapper<TpPayChannel> update = Wrappers.lambdaUpdate();
        update.set(TpPayChannel::getStatus, 1).eq(TpPayChannel::getId, tpPayChannel.getId());
        tpPayChannelService.update(new TpPayChannel(), update);
      }
    }
  }

  private boolean validateLimitAmount(String limitInfo, BigDecimal amount) {
    if (StringUtils.isNotBlank(limitInfo)) {
      ChannelLimitsBean limitsBean = ChannelLimitsBean.conver2Bean(limitInfo);
      if (TrueFalse.TRUE.getValue() == limitsBean.getLimitStatus()) {
        return limitsBean.getLimitAmount().compareTo(amount) < 0;
      }
    }
    return false;
  }

  private RechargeOrder buildManualRechargeOrder(ManualRechargeOrderBo manualRechargeOrderBo) {
    RechargeOrder rechargeOrder = new RechargeOrder();
    // 人工入款，传memberId，会员ID
    // 校验会员账户状态
    Member member = memberService.getById(manualRechargeOrderBo.getMemberId());
    MemberInfo memberInfo = memberInfoService.getById(manualRechargeOrderBo.getMemberId());
    verifyUser(member, memberInfo, false);
    fillUserInfo(rechargeOrder, member, memberInfo);
    /* 校验充值金额 */
    verifyAmount(manualRechargeOrderBo.getAmount(), manualRechargeOrderBo.getDiscountAmount());
    rechargeOrder.setAmount(manualRechargeOrderBo.getAmount());
    rechargeOrder.setPayAmount(manualRechargeOrderBo.getAmount());
    boolean isDiscountIgnored = false;
    if (manualRechargeOrderBo.getPointFlag() == TrueFalse.TRUE.getValue()
        && manualRechargeOrderBo.getDiscountType() != null) {
      if (bizBlacklistFacade.isUserInBlacklist(
          rechargeOrder.getMemberId(), BizBlacklistType.RECHARGE_DISCOUNT)) {
        rechargeOrder.setDiscountType(null);
        rechargeOrder.setDiscountAmount(BigDecimal.ZERO);
        rechargeOrder.setDiscountDml(BigDecimal.ZERO);
        isDiscountIgnored = true;
      } else {
        // 计算充值优惠
        DiscountType discountType =
            discountTypeService.getByValue(manualRechargeOrderBo.getDiscountType());
        verifyDiscountType(discountType);
        rechargeOrder.setDiscountRechargeFlag(discountType.getRechargeFlag());
        rechargeOrder.setDiscountType(discountType.getValue());
        rechargeOrder.setDiscountAmount(manualRechargeOrderBo.getDiscountAmount());
        rechargeOrder.setDiscountDml(manualRechargeOrderBo.getDiscountDml());
      }
    } else {
      rechargeOrder.setDiscountAmount(BigDecimal.ZERO);
      rechargeOrder.setDiscountDml(BigDecimal.ZERO);
    }

    // 充值总金额 = 支付金额 + 优惠金额
    rechargeOrder.setTotalAmount(
        rechargeOrder.getPayAmount().add(rechargeOrder.getDiscountAmount()));

    // 计算打码量
    rechargeOrder.setDmlFlag(manualRechargeOrderBo.getDmlFlag());
    rechargeOrder.setNormalDml(manualRechargeOrderBo.getNormalDml());

    // 设置订单计算积分
    rechargeOrder.setPointFlag(manualRechargeOrderBo.getPointFlag());

    // 会员备注信息
    rechargeOrder.setRemarks(manualRechargeOrderBo.getRemarks());

    // 审核备注信息
    if (isDiscountIgnored) {
      StringBuilder sb = new StringBuilder("会员或层级在充值优惠黑名单中，不赠送优惠。");
      Optional.ofNullable(manualRechargeOrderBo.getAuditRemarks())
          .filter(StringUtils::isNotBlank)
          .ifPresent(e -> sb.append(String.format("[%s]", e)));
      rechargeOrder.setAuditRemarks(sb.toString());
    } else {
      rechargeOrder.setAuditRemarks(manualRechargeOrderBo.getAuditRemarks());
    }
    rechargeOrder.setMode(RechargeMode.MANUAL.getValue());

    // 设置订单为未处理状态
    rechargeOrder.setStatus(RechargeStatus.UNHANDLED.getValue());

    // 人工入款，使用系统订单号
    rechargeOrder.setOrderNo(String.valueOf(IdGeneratorSnowflake.getInstance().nextId()));

    // 人工入款 使用当前系统时间
    rechargeOrder.setCreateTime(new Date());
    // 设置充值订单用户类型
    rechargeOrder.setMemberType(member.getUserType());
    // 设置超时时间
    MemberRechargeLimit limit = limitInfoService.getRechargeLimit();
    rechargeOrder.setOrderExpireTime(limit.getOrderExpireTime());
    return rechargeOrder;
  }

  private void fillUserInfo(RechargeOrder rechargeOrder, Member member, MemberInfo memberInfo) {
    rechargeOrder.setMemberId(member.getId());
    rechargeOrder.setAccount(member.getAccount());
    rechargeOrder.setNickname(member.getRealName());
    rechargeOrder.setMemberLevel(member.getUserLevel().toString());
    rechargeOrder.setSuperId(member.getParentId());
    rechargeOrder.setSuperAccount(member.getParentName());
    rechargeOrder.setSuperPath(member.getSuperPath());
    rechargeOrder.setBalance(memberInfo.getBalance());
    rechargeOrder.setMemberType(member.getUserType());
  }

  private void verifyAmount(BigDecimal amount, BigDecimal discountAmount) {
    if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
      throw new ServiceException("无效的充值金额。");
    }
    if (amount.compareTo(BigDecimal.ZERO) == 0
        && (discountAmount == null || discountAmount.compareTo(BigDecimal.ZERO) <= 0)) {
      throw new ServiceException("无效的充值金额。");
    }
    // 系统最高配置金额
    String maxAmount = configService.getValue(MAX_RECHARGE_MONEY);
    if (amount.compareTo(new BigDecimal(maxAmount)) > 0) {
      throw new ServiceException("人工充值金额不能大于系统配置的最高金额：" + maxAmount);
    }

    String maxDiscount = configService.getValue(MAX_DISCOUNT_MONEY);
    if (discountAmount != null) {
      if (discountAmount.compareTo(new BigDecimal(maxDiscount)) > 0) {
        throw new ServiceException("人工充值优惠金额不能大于系统配置的最高金额：" + maxDiscount);
      }
    }
  }

  private void verifyDiscountType(DiscountType discountType) {
    Assert.notNull(discountType, "优惠类型已禁用");
    Assert.isTrue(SwitchStatusEnum.ENABLED.match(discountType.getStatus()), "优惠类型已禁用");
  }

  private SummaryVO amountSum(RechargeOrderQueryDTO dto) {
    LambdaQueryWrapper<RechargeOrder> queryHandle = Wrappers.lambdaQuery();
    SummaryVO summaryVO = new SummaryVO();
    queryHandle
        .eq(RechargeOrder::getStatus, RechargeStatus.HANDLED.getValue())
        .in(ObjectUtils.isNotNull(dto.getModeList()), RechargeOrder::getMode, dto.getModeList())
        .in(
            ObjectUtils.isNotEmpty(dto.getMemberType())
                && MemberEnums.Type.MEMBER.match(dto.getMemberType()),
            RechargeOrder::getMemberType,
            MemberEnums.Type.MEMBER.value(),
            MemberEnums.Type.PROMOTION.value())
        .eq(
            ObjectUtils.isNotEmpty(dto.getMemberType())
                && MemberEnums.Type.PROMOTION.match(dto.getMemberType()),
            RechargeOrder::getMemberType,
            dto.getMemberType())
        .in(
            ObjectUtils.isNotNull(dto.getMemberLevelList()),
            RechargeOrder::getMemberLevel,
            dto.getMemberLevelList());
    summaryVO.setAllHandledSum(rechargeOrderMapper.summaryRechargeOrder(queryHandle));

    LambdaQueryWrapper<RechargeOrder> queryUnHandle = Wrappers.lambdaQuery();
    queryUnHandle
        .eq(RechargeOrder::getStatus, RechargeStatus.UNHANDLED.getValue())
        .in(ObjectUtils.isNotNull(dto.getModeList()), RechargeOrder::getMode, dto.getModeList())
        .in(
            ObjectUtils.isNotNull(dto.getMemberLevelList()),
            RechargeOrder::getMemberLevel,
            dto.getMemberLevelList());
    summaryVO.setAllUnhandledSum(rechargeOrderMapper.summaryRechargeOrder(queryUnHandle));
    return summaryVO;
  }

  /** 获取某时间段内某代理下所有会员的充值数据 */
  @Override
  public List<JSONObject> getSpreadReport(
      List<SpreadUnion> list, String startTime, String endTime) {
    return rechargeOrderMapper.getSpreadReport(list, startTime, endTime);
  }

  @Override
  public List<ThreeRechReportVo> findThreeRechReport(GameRWDataReportDto dto) {
    List<RechargeOrder> list = rechargeOrderMapper.selectList(this.builderMemberTodayQuery(dto));
    return BeanUtil.copyToList(list, ThreeRechReportVo.class);
  }

  @Override
  public long getUntreatedRechargeCount() {
    return this.lambdaQuery().eq(RechargeOrder::getMode, 1).eq(RechargeOrder::getStatus, 1).count();
  }

  @Override
  public void expired(Long id) {
    RechargeOrder rechargeOrder = this.getById(id);
    if (RechargeStatus.UNHANDLED.match(rechargeOrder.getStatus())) {
      rechargeOrder.setStatus(RechargeStatus.CANCELLED.getValue());
      rechargeOrder.setAuditRemarks("自动取消");
      rechargeOrder.setAuditTime(new Date());

      rechargeOrder.setAuditorAccount("系统审批");
      rechargeOrder.setRemarks("自动取消");
      if (this.updateById(rechargeOrder)) {
        log.info("自动取消充值订单，订单号{}", rechargeOrder.getOrderNo());
        RechargeOrderHistory rechargeOrderHistory = new RechargeOrderHistory();
        BeanUtils.copyProperties(rechargeOrder, rechargeOrderHistory);
        rechargeOrderHistoryMapper.insert(rechargeOrderHistory);
      }
    }
  }

  @Async
  @Override
  public void batchMemberRecharge(
      UserEquipment clientInfo,
      List<RechargeMemberFileBean> strAccount,
      ManualRechargeOrderDto dto) {
    List<RechargeMemberFileBean> strAccountList = new ArrayList<>();
    // 根据vip 等级获取账号
    if (ObjectUtil.isNotEmpty(dto.getLevel()) || ObjectUtil.isNotEmpty(dto.getVip())) {
      List<RechargeMemberFileBean> accountStr =
              memberService.findMemberRechVip(dto.getLevel(), dto.getVip());
      if (ObjectUtil.isNotEmpty(accountStr)) {
        strAccountList.addAll(accountStr);
      }
    }
    if (ObjectUtil.isNotEmpty(strAccount)) {
      strAccountList.addAll(strAccount);
    }
    if (ObjectUtil.isEmpty(strAccountList)) {
      return;
    }

    strAccountList = strAccountList.stream().distinct().collect(Collectors.toList());

    strAccountList.forEach(
        a -> {
          MemberBalanceVO memberVip = memberService.findMemberVip(a.getUsername());
          if (memberVip == null || StringUtils.isEmpty(memberVip.getAccount())) {
            return;
          }
          log.info("会员：{},充值金额:{}", a, dto.getAmount());
          dto.setAccount(memberVip.getAccount());
          ManualRechargeOrderBo manualRechargeOrderBo = new ManualRechargeOrderBo();
          BeanUtil.copyProperties(dto, manualRechargeOrderBo);
          manualRechargeOrderBo.setMemberId(memberVip.getId());
          try {
            manual(manualRechargeOrderBo, clientInfo);
          } catch (Exception e) {
            log.info("充值失败", e);
          }
        });
  }

  @Async
  @Override
  public void batchFileMemberRecharge(
      List<MemberRechBalanceVO> list, Integer discountType, UserEquipment userEquipment) {
    list.forEach(
        a -> {
          MemberInfoVO memberInfo = memberService.getMemberInfo(a.getAccount());
          if (memberInfo == null || StringUtils.isEmpty(memberInfo.getAccount())) {
            return;
          }

          // 开始创建订单，入款
          try {
            ManualRechargeOrderBo manualRechargeOrderBo =
                fillRechargeOrder(a, memberInfo, discountType);
            log.info("充值数据：{}", JSON.toJSONString(manualRechargeOrderBo));
            manual(manualRechargeOrderBo, userEquipment);
          } catch (Exception e) {
            e.printStackTrace();
          }
        });
  }

  private void add(
      Member member,
      BigDecimal balance,
      RechargeOrder rechargeOrder,
      String content,
      String operator,
      String... remark) {
    MemberBill bill = new MemberBill();
    bill.setBalance(balance);
    bill.setOrderNo(rechargeOrder.getOrderNo());
    bill.setTranType(RechargeMode.getTranType(rechargeOrder.getMode()));
    bill.setAmount(rechargeOrder.getTotalAmount());
    bill.setContent(content);
    bill.setOperator(operator);
    if (remark != null && remark.length > 0 && StringUtils.isNotEmpty(remark[0])) {
      bill.setRemark(remark[0]);
    }
    memberBillService.save(member, bill);
  }

  private void addUserBill(
      RechargeOrder rechargeOrder, Member member, BigDecimal balance, String operator) {
    StringBuilder sb = new StringBuilder();
    java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
    sb.append("订单编号 ")
        .append(rechargeOrder.getOrderNo())
        .append("，充值金额 ")
        .append(
            CNYUtils.formatYuanAsYuan(
                rechargeOrder.getPayAmount().setScale(2, BigDecimal.ROUND_DOWN)))
        .append("，优惠金额 ")
        .append(
            CNYUtils.formatYuanAsYuan(
                rechargeOrder.getDiscountAmount().setScale(2, BigDecimal.ROUND_DOWN)))
        .append("，余额 ")
        .append(
            df.format(
                balance.add(rechargeOrder.getTotalAmount().setScale(2, BigDecimal.ROUND_DOWN))));
    if (StringUtils.isNotEmpty(rechargeOrder.getPayType())) {
      sb.append("，支付类型 ").append(rechargeOrder.getPayTypeName());
    }
    if (rechargeOrder.getPayAccountId() != null) {
      sb.append("，收款账号 ").append(rechargeOrder.getPayAccountAccount());
      if (StringUtils.isNotEmpty(rechargeOrder.getPayAccountOwner())) {
        sb.append("，收款人 ").append(rechargeOrder.getPayAccountOwner());
      }
      if (StringUtils.isNotEmpty(rechargeOrder.getPayAccountBankName())) {
        sb.append("，收款银行 ").append(rechargeOrder.getPayAccountBankName());
      }
    }
    sb.append("。");
    if (StringUtils.isNotEmpty(rechargeOrder.getTpInterfaceCode())) {
      sb.append("第三方接口 ").append(rechargeOrder.getTpInterfaceName());
    }
    add(member, balance, rechargeOrder, sb.toString(), operator, "");
  }

  private void fillClientInfo(RechargeOrder rechargeOrder, UserEquipment clientInfo) {
    rechargeOrder.setBrowser(clientInfo.getUserAgent().getBrowser().getName());
    rechargeOrder.setOs(clientInfo.getUserAgent().getOs().getName());
    rechargeOrder.setIpAddress(clientInfo.getIpAddress());
    rechargeOrder.setUserAgent(clientInfo.getUserAgentString());
  }

  /** 查询在线支付 */
  private QueryWrapper<RechargeOrder> builderMemberTodayQuery(GameRWDataReportDto dto) {
    QueryWrapper<RechargeOrder> queryWrapper = new QueryWrapper<>();
    return queryWrapper
        .select("tp_interface_code,tp_interface_name,sum(amount) as amount")
        .eq("status", com.gameplat.common.enums.RechargeStatus.SUCCESS.getValue())
        .between("audit_time", dto.getStartTime(), dto.getEndTime())
        .eq("mode", com.gameplat.common.enums.RechargeStatus.HANDLED.getValue())
        .eq(StringUtils.isNotEmpty(dto.getAccount()), "account", dto.getAccount())
        .eq(
            StringUtils.isNotEmpty(dto.getSuperAccount()) && EnableEnum.isEnabled(dto.getFlag()),
            "super_account",
            dto.getSuperAccount())
        .eq(
            StringUtils.isNotEmpty(dto.getSuperAccount())
                && EnableEnum.DISABLED.match(dto.getFlag()),
            "super_path",
            dto.getSuperAccount())
        .groupBy("tp_interface_code");
  }

  private ManualRechargeOrderBo fillRechargeOrder(
      MemberRechBalanceVO memberRechBalanceVO, MemberInfoVO memberInfoVO, Integer discountType) {
    ManualRechargeOrderBo orderBo = new ManualRechargeOrderBo();
    orderBo.setMemberId(memberInfoVO.getId());
    orderBo.setAccount(memberRechBalanceVO.getAccount());
    orderBo.setPointFlag(1);
    orderBo.setRemarks(memberRechBalanceVO.getRemark());
    orderBo.setAuditRemarks(memberRechBalanceVO.getAuditRemarks());
    orderBo.setSkipAuditing(true);
    orderBo.setAmount(BigDecimal.ZERO);
    orderBo.setNormalDml(BigDecimal.ZERO);
    orderBo.setDiscountType(discountType);
    orderBo.setDiscountAmount(memberRechBalanceVO.getAmount());
    orderBo.setDiscountDml(
        memberRechBalanceVO.getBetMultiple().multiply(memberRechBalanceVO.getAmount()));
    orderBo.setDmlFlag(1);
    return orderBo;
  }
}
