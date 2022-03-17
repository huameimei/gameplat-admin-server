package com.gameplat.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
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
import com.gameplat.admin.enums.MemberEnums.Type;
import com.gameplat.admin.enums.RechargeStatus;
import com.gameplat.admin.enums.SysUserEnums;
import com.gameplat.admin.mapper.RechargeOrderHistoryMapper;
import com.gameplat.admin.mapper.RechargeOrderMapper;
import com.gameplat.admin.model.bean.*;
import com.gameplat.admin.model.dto.GameRWDataReportDto;
import com.gameplat.admin.model.dto.MemberActivationDTO;
import com.gameplat.admin.model.dto.RechargeOrderQueryDTO;
import com.gameplat.admin.model.vo.MemberActivationVO;
import com.gameplat.admin.model.vo.RechargeOrderVO;
import com.gameplat.admin.model.vo.SummaryVO;
import com.gameplat.admin.model.vo.ThreeRechReportVo;
import com.gameplat.admin.service.*;
import com.gameplat.admin.util.MoneyUtils;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.json.JsonUtils;
import com.gameplat.base.common.snowflake.IdGeneratorSnowflake;
import com.gameplat.base.common.util.DateUtil;
import com.gameplat.common.enums.BooleanEnum;
import com.gameplat.common.enums.MemberEnums;
import com.gameplat.common.enums.SwitchStatusEnum;
import com.gameplat.common.enums.UserTypes;
import com.gameplat.common.model.bean.UserEquipment;
import com.gameplat.common.model.bean.limit.MemberRechargeLimit;
import com.gameplat.model.entity.DiscountType;
import com.gameplat.model.entity.member.Member;
import com.gameplat.model.entity.member.MemberBill;
import com.gameplat.model.entity.member.MemberInfo;
import com.gameplat.model.entity.pay.PayAccount;
import com.gameplat.model.entity.pay.TpMerchant;
import com.gameplat.model.entity.pay.TpPayChannel;
import com.gameplat.model.entity.recharge.RechargeOrder;
import com.gameplat.model.entity.recharge.RechargeOrderHistory;
import com.gameplat.model.entity.spread.SpreadUnion;
import com.gameplat.model.entity.sys.SysUser;
import com.gameplat.security.SecurityUserHolder;
import com.gameplat.security.context.UserCredential;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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

  @Autowired(required = false) private RechargeOrderMapper rechargeOrderMapper;

  @Autowired(required = false) private RechargeOrderHistoryMapper rechargeOrderHistoryMapper;

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



  /**充值会员、代理 */
  private final String RECH_FORMAL_TYPE = "M";

  /**查询会员类型 */
  private final String RECH_FORMAL_TYPE_QUERY = "M,A";

  /**充值推广 */
  private final String RECH_TEST_TYPE = "P";





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
            ObjectUtils.isNotNull(dto.getPayAccountOwnerList()),
            RechargeOrder::getPayAccountOwner,
            dto.getPayAccountOwnerList())
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
            ObjectUtils.isNotEmpty(dto.getMemberType()) && dto.getMemberType().equalsIgnoreCase(RECH_FORMAL_TYPE),
            RechargeOrder::getMemberType,
                RECH_FORMAL_TYPE_QUERY.split(","))
         .eq(ObjectUtils.isNotEmpty(dto.getMemberType()) && dto.getMemberType().equalsIgnoreCase(RECH_TEST_TYPE),
                 RechargeOrder::getMemberType,dto.getMemberType())
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
        ObjectUtils.isEmpty(dto.getOrder()) ? false : dto.getOrder().equals("ASC"),
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
  public void handle(Long id, UserCredential userCredential) {
    // 更新充值订单状态
    this.updateStatus(
        id,
        RechargeStatus.UNHANDLED.getValue(),
        RechargeStatus.HANDLED.getValue(),
        userCredential.getUsername());
  }

  @Override
  public void unHandle(Long id, UserCredential userCredential) {
    RechargeOrder rechargeOrder = this.getById(id);
    // 校验已处理订单是否允许其他账户操作
    crossAccountCheck(userCredential, rechargeOrder);
    // 更新充值订单状态
    this.updateStatus(
        id,
        RechargeStatus.HANDLED.getValue(),
        RechargeStatus.UNHANDLED.getValue(),
        userCredential.getUsername());
  }

  @Override
  public void accept(Long id, UserCredential userCredential, String auditRemarks) throws Exception {
    RechargeOrder rechargeOrder = this.getById(id);
    // 校验订单状态
    verifyRechargeOrderForAuditing(rechargeOrder);
    // 校验已处理订单是否允许其他账户操作
    crossAccountCheck(userCredential, rechargeOrder);
    // 校验会员账户状态
    Member member = memberService.getById(rechargeOrder.getMemberId());
    MemberInfo memberInfo = memberInfoService.getById(rechargeOrder.getMemberId());
    verifyUser(member, memberInfo, rechargeOrder.getMode() == RechargeMode.TRANSFER.getValue());

    /** 校验子账号当日存款审核额度 */
    SysUser sysUser = null;
    if (null != userCredential) {
      sysUser = sysUserService.getByUsername(userCredential.getUsername());
    }
    AdminLimitInfo adminLimitInfo = JsonUtils.parse(sysUser.getLimitInfo(), AdminLimitInfo.class);
    if (null != sysUser && StringUtils.equals(UserTypes.SUBUSER.value(), sysUser.getUserType())) {
      checkZzhRechargeAmountAudit(
          userCredential.getUsername(),
          adminLimitInfo,
          rechargeOrder.getMode(),
          rechargeOrder.getTotalAmount());
    }

    // 重新校验首充优惠
    recalculateDiscountAmount(rechargeOrder, memberInfo.getTotalRechTimes() > 0);

    // 更新订单状态
    rechargeOrder.setAuditorAccount(userCredential.getUsername());
    rechargeOrder.setAuditTime(new Date());
    if (null != auditRemarks) {
      rechargeOrder.setAuditRemarks(auditRemarks);
    }
    rechargeOrder.setStatus(RechargeStatus.SUCCESS.getValue());
    updateRechargeOrder(rechargeOrder);
    // 更新充提报表
    //过滤 推广账号 充值
    if (!UserTypes.PROMOTION.value().equals(member.getUserType())) {
      memberRwReportService.addRecharge(member, memberInfo.getTotalRechTimes(), rechargeOrder);
    }

    if (rechargeOrder.getDmlFlag() == TrueFalse.TRUE.getValue()) {
      // 计算打码量
      validWithdrawService.addRechargeOrder(rechargeOrder);
    }
    // 添加会员账变
    addUserBill(rechargeOrder, member, memberInfo.getBalance(), userCredential.getUsername());
    // 更新充值金额累积
    updateRechargeMoney(rechargeOrder, member.getUserType());

    // 更新会员充值信息
    memberInfoService.updateBalanceWithRecharge(
        memberInfo.getMemberId(), rechargeOrder.getPayAmount(), rechargeOrder.getTotalAmount());

    // 判断充值是否计算积分
    if (TrueFalse.TRUE.getValue() != rechargeOrder.getPointFlag()) {
      log.info(
          "充值不增加成长值 account={}，orderNo={},pointFlag={}",
          rechargeOrder.getAccount(),
          rechargeOrder.getOrderNo(),
          rechargeOrder.getPointFlag());
      return;
    }
  }

  @Override
  public void cancel(Long id, UserCredential userCredential) throws ServiceException {
    RechargeOrder rechargeOrder = this.getById(id);
    // 校验订单状态
    verifyRechargeOrderForAuditing(rechargeOrder);
    // 校验已处理订单是否允许其他账户操作
    crossAccountCheck(userCredential, rechargeOrder);
    // 更新订单状态
    rechargeOrder.setAuditorAccount(userCredential.getUsername());
    rechargeOrder.setAuditTime(new Date());
    rechargeOrder.setStatus(RechargeStatus.CANCELLED.getValue());
    updateRechargeOrder(rechargeOrder);
  }

  @Override
  public void updateStatus(Long id, Integer curStatus, Integer newStatus, String auditorAccount) {
    LambdaUpdateWrapper<RechargeOrder> update = Wrappers.lambdaUpdate();
    update
        .set(RechargeOrder::getStatus, newStatus)
        .set(RechargeOrder::getAcceptAccount, auditorAccount)
        .set(RechargeOrder::getAcceptTime,new Date())
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

  /**
   * 后台入款 1、校验会员账户状态 2、计算充值优惠 3、计算打码量 4、填入模式、状态及订单号等信息 5、填入校验字段 6、设置为直接入款时进行入款操作
   *
   * @param manualRechargeOrderBo 封装人工入款信息
   * @param userCredential 操作员
   * @throws Exception 校验不通过抛出异常
   */
  @Override
  public void manual(ManualRechargeOrderBo manualRechargeOrderBo, UserCredential userCredential,UserEquipment userEquipment)
      throws Exception {

    RechargeOrder rechargeOrder = buildManualRechargeOrder(manualRechargeOrderBo);
    // 填充客户端信息
    fillClientInfo(rechargeOrder, userEquipment);
    this.save(rechargeOrder);

    if (manualRechargeOrderBo.isSkipAuditing()) {
      String auditRemarks =
          StringUtils.isBlank(manualRechargeOrderBo.getAuditRemarks()) ? null : "直接入款";
      accept(rechargeOrder.getId(), userCredential, auditRemarks);
    }
  }

  @Override
  public List<ActivityStatisticItem> findRechargeDateList(Map map) {
    List<ActivityStatisticItem> rechargeDateList = rechargeOrderMapper.findRechargeDateList(map);
    if (CollectionUtils.isNotEmpty(rechargeDateList)) {
      // 将逗号分隔的日期String转成List<Date>
      for (ActivityStatisticItem rechargeDate : rechargeDateList) {
        if (StringUtils.isNotEmpty(rechargeDate.getRechargeTimes())) {
          List<String> dateList = Arrays.asList(rechargeDate.getRechargeTimes().split(","));
          // 去重
          List<String> list = dateList.stream().distinct().collect(Collectors.toList());
          Collections.sort(list);
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
  public List<ActivityStatisticItem> findAllFirstRechargeAmount(Map map) {

    return null;
  }

  /** 根据会员和最后修改时间获取充值次数、充值金额、充值优惠、其它优惠 */
  @Override
  public MemberActivationVO getRechargeInfoByNameAndUpdateTime(
      MemberActivationDTO memberActivationDTO) {
    return rechargeOrderMapper.getRechargeInfoByNameAndUpdateTime(memberActivationDTO);
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
  public void crossAccountCheck(UserCredential userCredential, RechargeOrder rechargeOrder)
      throws ServiceException {
    MemberRechargeLimit limit = limitInfoService.getRechargeLimit();
    boolean toCheck =
        BooleanEnum.NO.match(limit.getIsHandledAllowOthersOperate())
            && !userCredential.isSuperAdmin();
    if (toCheck) {
      if (RechargeStatus.HANDLED.getValue() != rechargeOrder.getStatus()
          && !userCredential.getUsername().equals(rechargeOrder.getAuditorAccount())) {
        throw new ServiceException("您无权操作此订单:" + rechargeOrder.getOrderNo());
      }
    }
  }

  private void verifyUser(Member member, MemberInfo memberInfo, boolean checkLimitRech)
      throws Exception {
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
    if (Type.TEST.match(member.getUserType())) {
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

  private void updateRechargeMoney(RechargeOrder rechargeOrder, String userType) throws Exception {
    /** 收款账号通道金额更新 */
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
      /** 校验收款通道充值总金额 */
      if (validateLimitAmount(payAccount.getLimitInfo(), payAccount.getRechargeAmount())) {
        LambdaUpdateWrapper<PayAccount> update = Wrappers.lambdaUpdate();
        update
            .set(PayAccount::getStatus, SwitchStatusEnum.DISABLED.getValue())
            .eq(PayAccount::getId, payAccount.getId());
        payAccountService.update(new PayAccount(), update);
      }
    }

    /** 在线商户充值金额更新 */
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

    /** 在线通道充值总金额更新 */
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
      /** 校验在线通道充值总金额 */
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
        if (limitsBean.getLimitAmount().compareTo(amount) < 0) {
          return true;
        }
      }
    }
    return false;
  }

  private RechargeOrder buildManualRechargeOrder(ManualRechargeOrderBo manualRechargeOrderBo)
      throws Exception {
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
    rechargeOrder.setTotalAmount(rechargeOrder.getPayAmount().add(rechargeOrder.getDiscountAmount()));

    // 计算打码量
    rechargeOrder.setDmlFlag(manualRechargeOrderBo.getDmlFlag());
    rechargeOrder.setNormalDml(manualRechargeOrderBo.getNormalDml());

    // 设置订单计算积分
    rechargeOrder.setPointFlag(manualRechargeOrderBo.getPointFlag());
    /** 会员备注信息 */
    rechargeOrder.setRemarks(manualRechargeOrderBo.getRemarks());
    /** 审核备注信息 */
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

  private void verifyAmount(BigDecimal amount, BigDecimal discountAmount) throws Exception {
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

  private void verifyDiscountType(DiscountType discountType) throws Exception {
    if (discountType == null) {
      throw new ServiceException("优惠类型已禁用。");
    }
    if (discountType.getStatus() != SwitchStatusEnum.ENABLED.getValue()) {
      throw new ServiceException("优惠类型已禁用。");
    }
  }



  private SummaryVO amountSum(RechargeOrderQueryDTO dto) {
    LambdaQueryWrapper<RechargeOrder> queryHandle = Wrappers.lambdaQuery();
    SummaryVO summaryVO = new SummaryVO();
    queryHandle
        .eq(RechargeOrder::getStatus, RechargeStatus.HANDLED.getValue())
        .in(ObjectUtils.isNotNull(dto.getModeList()), RechargeOrder::getMode, dto.getModeList())
        .in(ObjectUtils.isNotEmpty(dto.getMemberType()) && dto.getMemberType().equalsIgnoreCase(RECH_FORMAL_TYPE),
                RechargeOrder::getMemberType,
                RECH_FORMAL_TYPE_QUERY.split(","))
        .eq(ObjectUtils.isNotEmpty(dto.getMemberType()) && dto.getMemberType().equalsIgnoreCase(RECH_TEST_TYPE),
                RechargeOrder::getMemberType,dto.getMemberType())
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

  private void addUserBill(
      RechargeOrder rechargeOrder, Member member, BigDecimal balance, String operator)
      throws Exception {
    StringBuilder sb = new StringBuilder();
    java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
    sb.append("订单编号 ")
        .append(rechargeOrder.getOrderNo())
        .append("，充值金额 ")
        .append(rechargeOrder.getPayAmount())
        .append("，优惠金额 ")
        .append(rechargeOrder.getDiscountAmount())
        .append("，余额 ")
        .append(df.format(balance.add(rechargeOrder.getTotalAmount())));
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

  public void add(
      Member member,
      BigDecimal balance,
      RechargeOrder rechargeOrder,
      String content,
      String operator,
      String... remark)
      throws Exception {
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

  private void fillClientInfo(RechargeOrder rechargeOrder, UserEquipment clientInfo) {
    rechargeOrder.setBrowser(clientInfo.getUserAgent().getBrowser().getName());
    rechargeOrder.setOs(clientInfo.getUserAgent().getOs().getName());
    rechargeOrder.setIpAddress(clientInfo.getIpAddress());
    rechargeOrder.setUserAgent(clientInfo.getUserAgentString());
  }

  /** 获取某时间段内某代理下所有会员的充值数据 */
  @Override
  public List<JSONObject> getSpreadReport(
      List<SpreadUnion> list, String startTime, String endTime) {
    return rechargeOrderMapper.getSpreadReport(list, startTime, endTime);
  }


  @Override
  public List<ThreeRechReportVo> findThreeRechReport(GameRWDataReportDto dto){
      List<RechargeOrder> list =  rechargeOrderMapper.selectList(this.builderMemberTodayQuery(dto));
      return BeanUtil.copyToList(list, ThreeRechReportVo.class);
  }

  private final int ONE = 1;

  private final int ZOO = 0;

  /**
   * 查询在线支付
   */
  private QueryWrapper<RechargeOrder> builderMemberTodayQuery(GameRWDataReportDto dto) {
      QueryWrapper<RechargeOrder> queryWrapper = new QueryWrapper<>();
      return queryWrapper.select("tp_interface_code,tp_interface_name,sum(amount) as amount")
              .eq("status", com.gameplat.common.enums.RechargeStatus.SUCCESS.getValue())
              .between("audit_time", dto.getStartTime(), dto.getEndTime())
              .eq("mode", com.gameplat.common.enums.RechargeStatus.HANDLED.getValue())
              .eq(StringUtils.isNotEmpty(dto.getAccount()),"account", dto.getAccount())
              .eq(StringUtils.isNotEmpty(dto.getSuperAccount()) && ONE == dto.getFlag(),"super_account", dto.getSuperAccount())
              .eq(StringUtils.isNotEmpty(dto.getSuperAccount()) && ZOO == dto.getFlag(),"super_path", dto.getSuperAccount())
              .groupBy("tp_interface_code");

  }
}
