package com.gameplat.admin.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.constant.WithdrawTypeConstant;
import com.gameplat.admin.convert.MemberWithdrawConvert;
import com.gameplat.admin.enums.*;
import com.gameplat.admin.feign.PaymentCenterFeign;
import com.gameplat.admin.mapper.MemberWithdrawMapper;
import com.gameplat.admin.model.bean.AdminLimitInfo;
import com.gameplat.admin.model.bean.ProxyDispatchContext;
import com.gameplat.admin.model.domain.*;
import com.gameplat.admin.model.domain.limit.MemberRechargeLimit;
import com.gameplat.admin.model.dto.UserWithdrawQueryDTO;
import com.gameplat.admin.model.vo.MemberWithdrawVO;
import com.gameplat.admin.model.vo.PpMerchantVO;
import com.gameplat.admin.service.*;
import com.gameplat.admin.util.MoneyUtils;
import com.gameplat.common.enums.UserTypes;
import com.gameplat.common.exception.ServiceException;
import com.gameplat.common.json.JsonUtils;
import com.gameplat.common.snowflake.IdGeneratorSnowflake;
import com.gameplat.common.util.StringUtils;
import com.gameplat.security.context.UserCredential;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class MemberWithdrawServiceImpl extends ServiceImpl<MemberWithdrawMapper, MemberWithdraw>
    implements MemberWithdrawService {

  @Autowired private MemberWithdrawConvert userWithdrawConvert;

  @Autowired private MemberWithdrawMapper userWithdrawMapper;

  @Autowired private MemberWithdrawHistoryService memberWithdrawHistoryService;

  @Autowired private MemberService memberService;

  @Autowired private SysUserService sysUserService;

  @Autowired private MemberBalanceService memberBalanceService;

  @Autowired private LimitInfoService limitInfoService;

  @Autowired private PpInterfaceService ppInterfaceService;

  @Autowired private PpMerchantService ppMerchantService;

  @Autowired private MemberInfoService memberInfoService;

  @Autowired private PaymentCenterFeign paymentCenterFeign;

  @Override
  public IPage<MemberWithdrawVO> findPage(Page<MemberWithdraw> page, UserWithdrawQueryDTO dto) {
    LambdaQueryWrapper<MemberWithdraw> query = Wrappers.lambdaQuery();
    query
        .in(
            ObjectUtils.isNotNull(dto.getBankNameList()),
            MemberWithdraw::getBankName,
            dto.getBankNameList())
        .in(
            ObjectUtils.isNotNull(dto.getCashStatusList()),
            MemberWithdraw::getCashStatus,
            dto.getCashStatusList())
        .eq(
            ObjectUtils.isNotEmpty(dto.getSuperName()),
            MemberWithdraw::getSuperName,
            dto.getSuperName())
        .eq(
            ObjectUtils.isNotEmpty(dto.getBankCard()),
            MemberWithdraw::getBankCard,
            dto.getBankCard())
        .eq(ObjectUtils.isNotEmpty(dto.getAccount()), MemberWithdraw::getAccount, dto.getAccount())
        .ge(
            ObjectUtils.isNotEmpty(dto.getCashMoneyFrom()),
            MemberWithdraw::getCashMoney,
            dto.getCashMoneyFrom())
        .le(
            ObjectUtils.isNotEmpty(dto.getCashMoneyFromTo()),
            MemberWithdraw::getCashMoney,
            dto.getCashMoneyFromTo())
        .eq(
            ObjectUtils.isNotEmpty(dto.getMemberType()),
            MemberWithdraw::getMemberType,
            dto.getMemberType())
        .eq(
            ObjectUtils.isNotEmpty(dto.getCashOrderNo()),
            MemberWithdraw::getCashOrderNo,
            dto.getCashOrderNo())
        .eq(
            ObjectUtils.isNotEmpty(dto.getOperatorAccount()),
            MemberWithdraw::getOperatorAccount,
            dto.getOperatorAccount())
        .ge(
            ObjectUtils.isNotNull(dto.getCreateTimeFrom()),
            MemberWithdraw::getCreateTime,
            dto.getCreateTimeFrom())
        .le(
            ObjectUtils.isNotNull(dto.getCreateTimeTo()),
            MemberWithdraw::getCreateTime,
            dto.getCreateTimeTo())
        .in(
            ObjectUtils.isNotNull(dto.getMemberLevelList()),
            MemberWithdraw::getMemberLevel,
            dto.getMemberLevelList());
    if (ObjectUtils.isNotNull(dto.getRechargeStatusList())
        && dto.getRechargeStatusList().size() > 0) {
      query
          .eq(dto.getRechargeStatusList().contains(3), MemberWithdraw::getWithdrawType, "BANK")
          .eq(dto.getRechargeStatusList().contains(4), MemberWithdraw::getWithdrawType, "DIRECT")
          .notIn(
              dto.getRechargeStatusList().contains(5),
              MemberWithdraw::getWithdrawType,
              "BANK",
              "MANUAL",
              "DIRECT")
          .gt(dto.getRechargeStatusList().contains(6), MemberWithdraw::getCounterFee, 0);
    }
    query.orderBy(
        ObjectUtils.isNotEmpty(dto.getOrder()),
        dto.getOrder().equals("ASC"),
        MemberWithdraw::getCreateTime);
    return this.page(page, query).convert(userWithdrawConvert::toVo);
  }

  @Override
  public void updateCounterFee(Long id, BigDecimal afterCounterFee) {
    if (null == afterCounterFee || afterCounterFee.compareTo(BigDecimal.ZERO) <= 0) {
      throw new ServiceException("调整后的手续费不能为空或者为负数，请检查！");
    }
    MemberWithdraw memberWithdraw = this.getById(id);
    BigDecimal approveMoney = memberWithdraw.getCashMoney().subtract(afterCounterFee);
    BigDecimal approveCurrencyCount = BigDecimal.ZERO;
    if (Objects.nonNull(memberWithdraw.getWithdrawType())
        && !memberWithdraw.getWithdrawType().equals(WithdrawTypeConstant.BANK_SUB_TYPE)
        && !memberWithdraw.getWithdrawType().equals(WithdrawTypeConstant.DIRECT_SUB_TYPE)) {
      approveCurrencyCount = approveMoney.divide(memberWithdraw.getCurrencyRate());
    }
    LambdaUpdateWrapper<MemberWithdraw> update = Wrappers.lambdaUpdate();
    update
        .set(MemberWithdraw::getCounterFee, afterCounterFee)
        .set(MemberWithdraw::getApproveMoney, approveMoney)
        .set(MemberWithdraw::getApproveCurrencyCount, approveCurrencyCount)
        .eq(MemberWithdraw::getId, id);
    if (!this.update(new MemberWithdraw(), update)) {
      log.error("更新提现订单手续费：id=" + id + ",调整后手续费afterCounterFee=" + afterCounterFee);
      throw new ServiceException("UW/UPDATE_ERROR, 订单已处理", null);
    }
  }

  @Override
  public void updateRemarks(Long id, String cashReason) {
    if (null == cashReason) {
      throw new ServiceException("备注信息不能为空!");
    }
    LambdaUpdateWrapper<MemberWithdraw> update = Wrappers.lambdaUpdate();
    update.set(MemberWithdraw::getCashReason, cashReason).eq(MemberWithdraw::getId, id);
    this.update(new MemberWithdraw(), update);
  }

  @Override
  public void modify(
      Long id,
      Integer cashStatus,
      Integer curStatus,
      boolean isDirect,
      String approveReason,
      UserCredential userCredential) {
    if (cashStatus.equals(curStatus) || null == id || null == curStatus) {
      throw new ServiceException("错误的参数.");
    }
    MemberWithdraw memberWithdraw = userWithdrawMapper.selectById(id);

    if (memberWithdraw == null) {
      throw new ServiceException("UW/ORDER_NULL,充值订单不存在或订单已处理", null);
    }

    Integer origCashStatus = memberWithdraw.getCashStatus();
    if (!curStatus.equals(origCashStatus)) {
      throw new ServiceException("UW/ORDER_PROCESSED,订单状态已变化,请刷新重试.", null);
    }

    boolean isFinishedOrder =
        (WithdrawStatus.SUCCESS.getValue() == curStatus
            || WithdrawStatus.CANCELLED.getValue() == curStatus
            || WithdrawStatus.REFUSE.getValue() == curStatus);
    if (isFinishedOrder) {
      throw new ServiceException("UW/ORDER_NULL,出款订单已完成,请确认再试", null);
    }

    Member info = memberService.getById(memberWithdraw.getMemberId());
    if (info == null) {
      log.error("用户ID不存在:" + memberWithdraw.getMemberId());
      throw new ServiceException("UC/EXT_INFO_NULL,用户不存在", null);
    }
    if (WithdrawStatus.UNHANDLED.getValue() != curStatus) {
      // 验证已受理出款订单是否开启允许其他账户验证

      crossAccountCheck(userCredential, memberWithdraw);
    }
    /** 校验子账号当天受理会员取款审核额度 */
    if (null != userCredential.getUsername()
        && StringUtils.equals(UserTypes.SUBUSER.value(), userCredential.getUsername())
        && WithdrawStatus.SUCCESS.getValue() == cashStatus) {
      SysUser sysUser = null;
      sysUser = sysUserService.getByUsername(userCredential.getUsername());
      AdminLimitInfo adminLimitInfo = JsonUtils.parse(sysUser.getLimitInfo(), AdminLimitInfo.class);
      checkZzhWithdrawAmountAudit(
          adminLimitInfo,
          memberWithdraw.getCashMode(),
          memberWithdraw.getCashMoney(),
          userCredential.getUsername());
    }

    if (isDirect) {
      approveReason = approveReason != null ? approveReason + "(免提直充)" : "免提直充";
    }
    memberWithdraw.setCashStatus(cashStatus);
    memberWithdraw.setApproveReason(approveReason);
    memberWithdraw.setOperatorAccount(userCredential.getUsername());
    // 修改订单状态
    updateWithdraw(memberWithdraw, origCashStatus);
    boolean toFinishCurrentOrder =
        (WithdrawStatus.SUCCESS.getValue() == cashStatus
            || WithdrawStatus.CANCELLED.getValue() == cashStatus
            || WithdrawStatus.REFUSE.getValue() == cashStatus);
    if (WithdrawStatus.HANDLED.getValue() == cashStatus) {
      log.info("提现订单受理中 ，订单号为：{}", memberWithdraw.getCashOrderNo());
    } else if (WithdrawStatus.UNHANDLED.getValue() == cashStatus) {
      log.info("放弃受理提现订单,订单号为：{}", memberWithdraw.getCashOrderNo());
    } else if (Objects.equals(
        ProxyPayStatusEnum.PAY_PROGRESS.getCode(), memberWithdraw.getProxyPayStatus())) {
      log.info("提现订单 第三方出款中 ，订单号为：{}", memberWithdraw.getCashOrderNo());
    } else if (toFinishCurrentOrder) {
      if (WithdrawStatus.SUCCESS.getValue() == cashStatus) {
        // 扣除会员余额
        memberBalanceService.updateBalance(info.getId(), memberWithdraw.getCashMoney().negate());
        // 更新充提报表,如果是推广账户不进入报表
        // TODO: 2021/11/2  未完成
        // 删除出款验证打码量记录的数据
        // TODO: 2021/11/2  未完成
        // 免提直充
        // TODO: 2021/11/2  未完成
        //        if (isDirect) {
        //          this.directCharge(userWithdraw, operatorAccount);
        //        }

      } else if (WithdrawStatus.CANCELLED.getValue() == cashStatus) { // 取消出款操作
        //        UserMoneyBean userMoneyBean = new UserMoneyBean(userWithdraw.getUserId());
        //        userMoneyBean.setMoney(userWithdraw.getCashMoney());
        //        this.userService.updateMoney(userMoneyBean);
        // 释放会员提现金额
        memberBalanceService.updateBalance(info.getId(), memberWithdraw.getCashMoney());
        //        String billContent = String.format("管理员于%s向用户%s提现失败退回%.3f元,账户余额变更为:%.3f元",
        //            DateUtil.getNowTime(),
        //            info.getAccount(),
        //            userWithdraw.getCashMoney(),
        //            (userExtInfo.getMoney() + userWithdraw.getCashMoney())
        //        );
        //        UserBill bill = new UserBill();
        //        bill.setBalance(userExtInfo.getMoney());
        //        bill.setOrderNo(userWithdraw.getCashOrderNo());
        //        bill.setTranType(TranTypes.WITHDRAW_FAIL.getValue());
        //        bill.setMoney(userWithdraw.getCashMoney());
        //        bill.setContent(billContent);
        //        bill.setOperateName(operatorAccount.getAccount());
        //        userBillService.save(userService.getUserInfo(info.getUserId()), bill);
        //        if (isPush) {
        //          String content = String.format("您于%s提交的取现订单被取消，订单号为%s",
        //              DateUtil.getDateToString(userWithdraw.getAddTime(),
        // DateUtil.YYYY_MM_DD_HH_MM_SS),
        //              userWithdraw.getCashOrderNo());
        //          if (StringUtil.isNotBlank(userWithdraw.getApproveReason())) {
        //            content += String.format(",取消原因:%s", userWithdraw.getApproveReason());
        //          }
        //          pushMessageService.saveByCashier(userWithdraw.getUserId(), content);
        //        }
      } else if (WithdrawStatus.REFUSE.getValue() == cashStatus) {
        //        String content = String.format("您于%s提交的取现订单被没收，订单号为%s，金额：%s",
        //            DateUtil.getDateToString(userWithdraw.getAddTime(),
        // DateUtil.YYYY_MM_DD_HH_MM_SS),
        //            userWithdraw.getCashOrderNo(),
        //            userWithdraw.getCashMoney());
        //        UserBill bill = new UserBill();
        //        bill.setBalance(userExtInfo.getMoney());
        //        bill.setOrderNo(userWithdraw.getCashOrderNo());
        //        bill.setTranType(TranTypes.WITHDRAW_FAIL.getValue());
        //        bill.setMoney(0D);
        //        bill.setContent(content);
        //        bill.setOperateName(operatorAccount.getAccount());
        //        userBillService.save(userService.getUserInfo(info.getUserId()), bill);
        //        if (isPush) {
        //          pushMessageService.saveByCashier(userWithdraw.getUserId(), content);
        //        }
      }
      // 新增出款记录
      insertWithdrawHistory(memberWithdraw);
      //    }
    }
  }

  @Override
  public void proxyPay(
      Long id,
      Long ppMerchantId,
      String asyncCallbackUrl,
      String sysPath,
      UserCredential userCredential)
      throws Exception {
    MemberWithdraw memberWithdraw = this.getById(id);
    if (null == memberWithdraw) {
      throw new ServiceException("不存在的记录");
    }
    // 校验已受理出款是否允许其他人操作
    crossAccountCheck(userCredential, memberWithdraw);

    // 校验子账号当天取款审核额度
    SysUser sysUser = sysUserService.getByUsername(userCredential.getUsername());
    AdminLimitInfo adminLimitInfo = JsonUtils.parse(sysUser.getLimitInfo(), AdminLimitInfo.class);
    if (null != adminLimitInfo
        && StringUtils.equals(UserTypes.SUBUSER.value(), sysUser.getUserType())) {
      checkZzhWithdrawAmountAudit(
          adminLimitInfo,
          memberWithdraw.getCashMode(),
          memberWithdraw.getCashMoney(),
          userCredential.getUsername());
    }

    // 第三方代付商户
    PpMerchantVO ppMerchantVO = ppMerchantService.getPpMerchantById(ppMerchantId);
    if (!Optional.ofNullable(ppMerchantVO).isPresent()) {
      throw new ServiceException("请确认出款商户是否存在！");
    }

    // 代付接口信息
    PpInterface ppInterface = ppInterfaceService.get(ppMerchantVO.getPpInterfaceCode());
    verifyPpInterface(ppInterface);
    if (verifyPpMerchant(memberWithdraw, ppMerchantVO, ppInterface)) {
      throw new ServiceException("出款商户限制，请选择其他方式出款！");
    }

    // 封装第三方代付接口调用信息
    ProxyDispatchContext context = new ProxyDispatchContext();
    String asyncUrl = asyncCallbackUrl.replace("relProxyPay", "proxyPayAsyncCallback");
    context.setAsyncCallbackUrl(asyncUrl + "/" + memberWithdraw.getCashOrderNo());
    context.setSysPath(sysPath);
    // 设置会员真实姓名
    context.setName(memberWithdraw.getNickname());
    // 设置第三方接口信息
    fillProxyDispatchContext(context, ppInterface);
    // 设置银行编码,名称
    context.setBankCode(getProxyPayBankCode(memberWithdraw, ppMerchantVO, ppInterface));
    context.setBankName(memberWithdraw.getBankName());

    // 设置第三方商户参数
    context.setMerchantParameters(JSONObject.parseObject(ppMerchantVO.getParameters(), Map.class));

    // 设置客户请求地址
    context.setUserIpAddress(userCredential.getLoginIp());
    // 设置第三方商户出款数据
    buildWithdrawWithProxyMsg(memberWithdraw, ppMerchantVO, ppInterface);
    /** 第三方订单提交成功，将订单状态改为已出款 */
    memberWithdraw.setApproveReason(ppMerchantVO.getName() + "代付出款");
    memberWithdraw.setOperatorTime(new Date());
    memberWithdraw.setOperatorAccount(userCredential.getUsername());
    updateProxyWithdrawStatus(memberWithdraw, WithdrawStatus.SUCCESS.getValue());

    /** 请求第三方代付接口 */
    log.info("进入第三方出入款中心出款订单号: {}", memberWithdraw.getCashOrderNo());
    paymentCenterFeign.onlineProxyPay(
        context, memberWithdraw.getPpInterface(), memberWithdraw.getPpInterfaceName());
    /** 不需要异步通知，直接将状态改成第三方出款完成 */
    if (0 != ppInterface.getAsynNotifyStatus()) {
      setProxyPayStatus(memberWithdraw);
      this.lambdaUpdate()
          .set(MemberWithdraw::getCashStatus, WithdrawStatus.SUCCESS.getValue())
          .eq(MemberWithdraw::getId, memberWithdraw.getId())
          .update();

      /** 历史提现订单记录 */
      memberWithdrawHistoryService
          .lambdaUpdate()
          .set(MemberWithdrawHistory::getCashStatus, WithdrawStatus.SUCCESS.getValue())
          .eq(MemberWithdrawHistory::getId, memberWithdraw.getId())
          .update();

      /** 更新代付商户出款次数和金额 */
      ppMerchantService
          .lambdaUpdate()
          .set(PpMerchant::getProxyTimes, ppMerchantVO.getProxyTimes() + 1)
          .set(
              PpMerchant::getProxyAmount,
              ppMerchantVO.getProxyAmount().add(memberWithdraw.getCashMoney()))
          .eq(PpMerchant::getId, ppMerchantVO.getId())
          .update();
      log.info("第三方出款成功,出款商户为:{} ,出款订单信息为：{}", memberWithdraw.getPpMerchantName(), memberWithdraw);
    }
  }

  /** 过滤不符合规则的第三方出款商户 */
  @Override
  public List<PpMerchantVO> queryProxyMerchant(Long id) {
    // 根据体现记录查询用户的层级和出款金额
    MemberWithdraw memberWithdraw = this.getById(id);
    if (null == memberWithdraw) {
      throw new ServiceException("不存在的记录");
    }

    // 获取所有的可用代付商户
    List<PpMerchantVO> merchantVOList = ppMerchantService.queryAllMerchant(1);
    if (CollectionUtils.isEmpty(merchantVOList)) {
      throw new ServiceException("没有可用的代付商户");
    }
    // 根据用户体现信息，过滤相关代付相符
    Iterator<PpMerchantVO> iterator = merchantVOList.iterator();
    while (iterator.hasNext()) {
      PpMerchantVO ppMerchantVO = iterator.next();
      PpInterface ppInterface = ppInterfaceService.get(ppMerchantVO.getPpInterfaceCode());

      if (verifyPpMerchant(memberWithdraw, ppMerchantVO, ppInterface)) {
        iterator.remove();
      }
    }
    return merchantVOList;
  }

  @Override
  public void save(
      BigDecimal cashMoney, String cashReason, Integer handPoints, UserCredential userCredential)
      throws Exception {
    Member member = memberService.getById(userCredential.getUserId()); // 更新金额，从数据库中重新获取
    MemberInfo memberInfo = memberInfoService.getById(userCredential.getUserId()); // 更新金额，从数据库中重新获取
    // 校验用户状态
    checkUserInfo(member, memberInfo, false);
    // 判断金额是否为负数或者为0
    if (cashMoney.compareTo(BigDecimal.ZERO) <= 0) {
      throw new ServiceException("MinusError");
    }

    // 检查取款金额是否超过用户余额
    if (cashMoney.compareTo(memberInfo.getBalance()) > 0) {
      throw new ServiceException("取款额度不能超过余额");
    }

    /** 校验子账号当天受理人工取款审核额度 */
    SysUser sysUser = sysUserService.getByUsername(userCredential.getUsername());
    AdminLimitInfo adminLimitInfo = JsonUtils.parse(sysUser.getLimitInfo(), AdminLimitInfo.class);
    if (null != adminLimitInfo
        && StringUtils.equals(UserTypes.SUBUSER.value(), userCredential.getUserType())) {
      checkZzhWithdrawAmountAudit(
          adminLimitInfo,
          CashEnum.CASH_MODE_HAND.getValue(),
          cashMoney,
          userCredential.getUsername());
    }

    // 下面开始添加后台出款记录
    MemberWithdraw memberWithdraw = new MemberWithdraw();
    memberWithdraw.setCashOrderNo(String.valueOf(IdGeneratorSnowflake.getInstance().nextId()));
    memberWithdraw.setMemberId(userCredential.getUserId());
    memberWithdraw.setAccount(userCredential.getUsername());
    memberWithdraw.setNickname(userCredential.getRealName());
    memberWithdraw.setAccountMoney(memberInfo.getBalance());
    memberWithdraw.setCashMoney(cashMoney);
    memberWithdraw.setCashReason(cashReason);
    memberWithdraw.setCashMode(CashEnum.CASH_MODE_HAND.getValue());
    memberWithdraw.setCashStatus(WithdrawStatus.SUCCESS.getValue());
    memberWithdraw.setCreateTime(new Date());
    memberWithdraw.setCounterFee(BigDecimal.ZERO);
    memberWithdraw.setApproveMoney(cashMoney);
    memberWithdraw.setMemberLevel(member.getUserLevel().toString());
    memberWithdraw.setSuperId(member.getParentId());
    memberWithdraw.setSuperName(member.getParentName());
    memberWithdraw.setSuperPath(member.getSuperPath());
    memberWithdraw.setPoliceFlag(0);
    memberWithdraw.setMacOs(userCredential.getDeviceType());
    memberWithdraw.setUserAgent(userCredential.getUserAgent());
    memberWithdraw.setIpAddress(userCredential.getLoginIp());
    memberWithdraw.setOperatorAccount(userCredential.getUsername());
    memberWithdraw.setOperatorTime(new Date());
    memberWithdraw.setPointFlag(handPoints);
    memberWithdraw.setMemberType(member.getUserType());
    memberWithdraw.setWithdrawType(WithdrawTypeConstant.MANUAL); // 人工充值设置提现类型为人工充值
    this.save(memberWithdraw);
    // 添加取现历史记录
    insertWithdrawHistory(memberWithdraw);

    // 修改用户金额信息、扣除用户出款金额
    memberBalanceService.updateBalance(
        memberWithdraw.getMemberId(), memberWithdraw.getCashMoney().negate());
    memberInfoService.updateMemberWithdraw(memberInfo, memberWithdraw.getCashMoney());

    //    // 添加用户账户金额的变更记录
    //    String content = "管理员" /*+ admin.getAccount() */ + "于" + DateUtil.getNowTime() + "向用户"
    //        + userInfoVO.getUserInfo().getAccount() + "成功通过后台转出" + String.format("%.3f",
    // cashMoney)
    //        + "元,账户余额变更为:" + String.format("%.3f", (ext.getMoney() - cashMoney))
    //        + "元";
    //    UserBill bill = new UserBill();
    //    bill.setBalance(ext.getMoney());
    //    bill.setOrderNo(userWithdraw.getCashOrderNo());
    //    bill.setTranType(TranTypes.TRANSFER_OUT.getValue());
    //    bill.setMoney(-cashMoney);
    //    bill.setContent(content);
    //    bill.setOperateName(admin.getAccount());
    //    this.userBillService.save(userInfoVO.getUserInfo(), bill);
  }

  private void insertWithdrawHistory(MemberWithdraw memberWithdraw) {
    MemberWithdrawHistory memberWithdrawHistory = new MemberWithdrawHistory();
    BeanUtils.copyProperties(memberWithdraw, memberWithdrawHistory);
    memberWithdrawHistoryService.save(memberWithdrawHistory);
  }

  /**
   * 检验子账号出款受理额度
   *
   * @param adminLimitInfo
   * @param cashMode
   * @throws Exception
   */
  public void checkZzhWithdrawAmountAudit(
      AdminLimitInfo adminLimitInfo, Integer cashMode, BigDecimal cashMoney, String userName) {
    if (cashMode == CashEnum.CASH_MODE_USER.getValue()) {
      if (adminLimitInfo.getMaxWithdrawAmount().compareTo(BigDecimal.ZERO) > 0
          && cashMoney.compareTo(adminLimitInfo.getMaxWithdrawAmount()) > 0) {
        StringBuffer buffer =
            new StringBuffer(userName)
                .append("单笔出款受限。受理会员取款额度为：")
                .append(adminLimitInfo.getMaxWithdrawAmount())
                .append("元。 超过额度")
                .append(
                    MoneyUtils.toYuanStr(cashMoney.subtract(adminLimitInfo.getMaxWithdrawAmount())))
                .append("元");
        throw new ServiceException(buffer.toString());
      }
    } else if (cashMode == CashEnum.CASH_MODE_HAND.getValue()) {
      if (adminLimitInfo.getMaxManualWithdrawAmount().compareTo(BigDecimal.ZERO) > 0
          && cashMoney.compareTo(adminLimitInfo.getMaxManualWithdrawAmount()) > 0) {
        StringBuffer buffer =
            new StringBuffer(userName)
                .append("单笔人工出款受限。受理人工取款额度为")
                .append(adminLimitInfo.getMaxManualWithdrawAmount())
                .append("元。 超过额度")
                .append(
                    MoneyUtils.toYuanStr(
                        cashMoney.subtract(adminLimitInfo.getMaxManualWithdrawAmount())))
                .append("元");
        throw new ServiceException(buffer.toString());
      }
    }
  }

  private void updateWithdraw(MemberWithdraw memberWithdraw, Integer origCashStatus) {
    LambdaUpdateWrapper<MemberWithdraw> update = Wrappers.lambdaUpdate();
    update
        .set(MemberWithdraw::getCashStatus, memberWithdraw.getCashStatus())
        .set(MemberWithdraw::getApproveReason, memberWithdraw.getCashReason())
        .set(MemberWithdraw::getOperatorAccount, memberWithdraw.getOperatorAccount())
        .eq(MemberWithdraw::getId, memberWithdraw.getId())
        .eq(MemberWithdraw::getCashStatus, origCashStatus);
    this.update(update);
    if (!this.update(update)) {
      log.error(
          "修改提现订单异常：memberWithdraw="
              + memberWithdraw.toString()
              + ",origCashStatus="
              + origCashStatus);
      throw new ServiceException("UW/UPDATE_ERROR,订单已处理", null);
    }
  }

  /** 开启出入款订单是否允许其他账户操作配置 校验非超管账号是否原受理人 */
  private void crossAccountCheck(UserCredential userCredential, MemberWithdraw memberWithdraw)
      throws ServiceException {
    if (userCredential != null
        && StringUtils.isNotEmpty(userCredential.getUsername())
        && null != memberWithdraw) {
      MemberRechargeLimit limitInfo =
          Optional.ofNullable(
                  limitInfoService.getLimitInfo(
                      LimitEnums.MEMBER_RECHARGE_LIMIT.getName(), MemberRechargeLimit.class))
              .orElseThrow(() -> new ServiceException("加载出入款配置信息失败，请联系客服！"));
      boolean toCheck =
          (!Objects.equals(
                  AllowOthersOperateEnums.YES.getValue(),
                  limitInfo.getIsHandledAllowOthersOperate()))
              && !userCredential.isSuperAdmin();
      if (toCheck) {
        if (!Objects.equals(WithdrawStatus.UNHANDLED.getValue(), memberWithdraw.getCashStatus())
            && !StringUtils.equalsIgnoreCase(
                userCredential.getUsername(), memberWithdraw.getOperatorAccount())) {
          throw new ServiceException("您无权操作此订单:" + memberWithdraw.getCashOrderNo());
        }
      }
    }
  }

  private void verifyPpInterface(PpInterface ppInterface) {
    if (ppInterface == null) {
      throw new ServiceException("第三方代付接口已关闭。");
    }
    if (ppInterface.getStatus() != SwitchStatusEnum.ENABLED.getValue()) {
      throw new ServiceException("第三方代付接口已关闭。");
    }
  }

  private static boolean verifyPpMerchant(
      MemberWithdraw memberWithdraw, PpMerchantVO ppMerchantVO, PpInterface ppInterface) {
    if (memberWithdraw.getCashMoney().compareTo(ppMerchantVO.getMinLimitCash()) < 0
        || memberWithdraw.getCashMoney().compareTo(ppMerchantVO.getMaxLimitCash()) > 0) {
      log.info("用户出款金额超出商户出款金额范围，过滤此商户，商户名称为：" + ppMerchantVO.getName());
      return true;
    }

    if (StringUtils.isNotEmpty(ppMerchantVO.getUserLever())) {
      if (!StringUtils.contains(
          "," + ppMerchantVO.getUserLever() + ",",
          String.format("%s" + memberWithdraw.getMemberLevel() + "%s", ",", ","))) {
        log.info("用户层级不在此代付商户设置的层级中，过滤此商户，商户名称为：" + ppMerchantVO.getName());
        return true;
      }
    }

    Map<String, String> banksMap =
        JsonUtils.toObject(
            JSONObject.parseObject(ppInterface.getLimtInfo()).getString("banks"), Map.class);
    /** 模糊匹配银行名称 */
    boolean isBankName = true;
    for (Map.Entry<String, String> entry : banksMap.entrySet()) {
      if (StringUtils.contains(entry.getValue(), memberWithdraw.getBankName())
          || StringUtils.contains(memberWithdraw.getBankName(), entry.getValue())) {
        isBankName = false;
        break;
      }

      if (StringUtils.contains(entry.getValue(), "邮政")
          && StringUtils.contains(memberWithdraw.getBankName(), "邮政")) {
        isBankName = false;
        break;
      }
    }
    if (isBankName) {
      log.info("代付商户不支持用户银行卡出款，过滤此商户，商户名称为：" + ppMerchantVO.getName());
      return true;
    }
    return false;
  }

  private void fillProxyDispatchContext(ProxyDispatchContext context, PpInterface ppInterface) {
    context.setName(ppInterface.getName());
    context.setVersion(ppInterface.getVersion());
    context.setCharset(ppInterface.getCharset());
    context.setDispatchType(ppInterface.getDispatchType());
    context.setDispatchUrl(ppInterface.getDispatchUrl());
    context.setDispatchMethod(ppInterface.getDispatchMethod());
  }

  private String getProxyPayBankCode(
      MemberWithdraw memberWithdraw, PpMerchantVO merchantVO, PpInterface ppinterface) {
    if (null == merchantVO || SwitchStatusEnum.DISABLED.match(merchantVO.getStatus())) {
      throw new ServiceException("第三方代付商户已关闭");
    }

    String bankCode = "";
    String bankName = memberWithdraw.getBankName();
    Map<String, String> banksMap =
        JsonUtils.toObject(
            JSONObject.parseObject(ppinterface.getLimtInfo()).getString("banks"), Map.class);
    for (Map.Entry<String, String> entry : banksMap.entrySet()) {
      if (StringUtils.contains(entry.getValue(), bankName)
          || StringUtils.contains(bankName, entry.getValue())) {
        bankCode = entry.getKey();
      }
      if (StringUtils.contains(entry.getValue(), "邮政") && StringUtils.contains(bankName, "邮政")) {
        bankCode = entry.getKey();
      }
      // 处理银行名称模糊匹配无法正确设置对应code
      if (StringUtils.equals(bankName, entry.getValue())) {
        bankCode = entry.getKey();
        break;
      }
    }

    if (StringUtils.isBlank(bankCode)) {
      throw new ServiceException("银行编码错误，请检查银行配置信息！");
    }
    return bankCode;
  }

  private void buildWithdrawWithProxyMsg(
      MemberWithdraw memberWithdraw, PpMerchantVO ppMerchantVO, PpInterface ppInterface) {
    memberWithdraw.setProxyPayDesc("第三方出款中。。。");
    memberWithdraw.setProxyPayStatus(ProxyPayStatusEnum.PAY_PROGRESS.getCode());
    memberWithdraw.setPpInterface(ppInterface.getCode());
    memberWithdraw.setPpInterfaceName(ppInterface.getName());
    memberWithdraw.setPpMerchantId(ppInterface.getId());
    memberWithdraw.setPpMerchantName(ppInterface.getName());
  }

  /**
   * 第三方出款成功，改变出款状态
   *
   * @param memberWithdraw MemberWithdraw
   */
  public void updateProxyWithdrawStatus(MemberWithdraw memberWithdraw, Integer cashStatus) {
    // 修改订单状态
    LambdaUpdateWrapper<MemberWithdraw> updateMemberWithdraw = Wrappers.lambdaUpdate();
    updateMemberWithdraw
        .set(MemberWithdraw::getCashStatus, cashStatus)
        .set(MemberWithdraw::getApproveReason, memberWithdraw.getApproveReason())
        .set(MemberWithdraw::getOperatorAccount, memberWithdraw.getOperatorAccount())
        .set(MemberWithdraw::getOperatorTime, memberWithdraw.getOperatorTime())
        .set(MemberWithdraw::getProxyPayDesc, memberWithdraw.getProxyPayDesc())
        .set(MemberWithdraw::getProxyPayStatus, memberWithdraw.getProxyPayStatus())
        .set(MemberWithdraw::getPpInterface, memberWithdraw.getPpInterface())
        .set(MemberWithdraw::getPpInterfaceName, memberWithdraw.getPpInterfaceName())
        .set(MemberWithdraw::getPpMerchantId, memberWithdraw.getPpMerchantId())
        .set(MemberWithdraw::getPpMerchantName, memberWithdraw.getPpMerchantName())
        .eq(MemberWithdraw::getId, memberWithdraw.getId())
        .eq(MemberWithdraw::getCashStatus, memberWithdraw.getCashStatus());
    if (!this.update(updateMemberWithdraw)) {
      log.error(
          "修改提现订单异常：UserWithdraw="
              + memberWithdraw.toString()
              + ",origCashStatus="
              + memberWithdraw.getCashStatus());
      throw new ServiceException("UW/UPDATE_ERROR, 订单已处理", null);
    }
    // 更新会员信息表
    MemberInfo memberInfo = memberInfoService.getById(memberWithdraw.getMemberId());
    memberInfoService.updateMemberWithdraw(memberInfo, memberWithdraw.getCashMoney());

    // 删除出款验证打码量记录的数据
    //    validWithdrawService.remove(userWithdraw.getUserId(), userWithdraw.getAddTime());
    //
    // 添加取现历史记录
    memberWithdraw.setCashStatus(cashStatus);
    insertWithdrawHistory(memberWithdraw);
  }

  private void setProxyPayStatus(MemberWithdraw memberWithdraw) {
    memberWithdraw.setApproveReason("第三方出款成功");
    memberWithdraw.setProxyPayDesc("第三方出款成功");
    memberWithdraw.setProxyPayStatus(ProxyPayStatusEnum.PAY_SUCCESS.getCode());
  }

  /** 检查用户，封装用户信息 */
  private void checkUserInfo(Member member, MemberInfo memberInfo, boolean checkUserState) {
    // 查询用户是否存在
    if (member == null) {
      throw new ServiceException("UC/USER_NOT_EXIST, uc.user_not_exist", null);
    }
    // 查询用户的扩展信息是否为空
    if (memberInfo == null) {
      throw new ServiceException("UC/USER_NOT_EXIST, uc.user_not_exist", null);
    }
    if (checkUserState) {
      // 查询用户是否正常
      if (!UserStates.isMoneyDeal(member.getStatus())) {
        throw new ServiceException("用户已经被冻结");
      }
    }
    if (member.getUserType().equals(UserTypes.TEST.value())) {
      throw new ServiceException("用户为试玩会员");
    }
  }
}
