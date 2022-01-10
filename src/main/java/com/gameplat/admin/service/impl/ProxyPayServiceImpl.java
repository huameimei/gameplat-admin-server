package com.gameplat.admin.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gameplat.admin.enums.CashEnum;
import com.gameplat.admin.enums.ProxyPayStatusEnum;
import com.gameplat.admin.enums.WithdrawStatus;
import com.gameplat.admin.feign.PaymentCenterFeign;
import com.gameplat.admin.model.bean.AdminLimitInfo;
import com.gameplat.admin.model.bean.NameValuePair;
import com.gameplat.admin.model.bean.ProxyCallbackContext;
import com.gameplat.admin.model.bean.ProxyDispatchContext;
import com.gameplat.admin.model.bean.ProxyPayBackResult;
import com.gameplat.admin.model.bean.ProxyPayMerBean;
import com.gameplat.admin.model.bean.ReturnMessage;
import com.gameplat.admin.model.domain.Member;
import com.gameplat.admin.model.domain.MemberWithdraw;
import com.gameplat.admin.model.domain.MemberWithdrawHistory;
import com.gameplat.admin.model.domain.PpInterface;
import com.gameplat.admin.model.domain.PpMerchant;
import com.gameplat.admin.model.domain.SysUser;
import com.gameplat.admin.service.LimitInfoService;
import com.gameplat.admin.service.MemberInfoService;
import com.gameplat.admin.service.MemberService;
import com.gameplat.admin.service.MemberWithdrawHistoryService;
import com.gameplat.admin.service.MemberWithdrawService;
import com.gameplat.admin.service.PpInterfaceService;
import com.gameplat.admin.service.PpMerchantService;
import com.gameplat.admin.service.ProxyPayService;
import com.gameplat.admin.service.SysUserService;
import com.gameplat.admin.util.MoneyUtils;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.json.JsonUtils;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.enums.BooleanEnum;
import com.gameplat.common.enums.LimitEnums;
import com.gameplat.common.enums.SwitchStatusEnum;
import com.gameplat.common.enums.UserTypes;
import com.gameplat.common.model.bean.limit.MemberRechargeLimit;
import com.gameplat.security.context.UserCredential;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class ProxyPayServiceImpl implements ProxyPayService {

  @Autowired private MemberWithdrawService memberWithdrawService;

  @Autowired private MemberWithdrawHistoryService memberWithdrawHistoryService;

  @Autowired private MemberService memberService;

  @Autowired private SysUserService sysUserService;

  @Autowired private LimitInfoService limitInfoService;

  @Autowired private PpInterfaceService ppInterfaceService;

  @Autowired private PpMerchantService ppMerchantService;

  @Autowired private MemberInfoService memberInfoService;

  @Autowired private PaymentCenterFeign paymentCenterFeign;

  @Override
  public void proxyPay(
      Long id,
      Long ppMerchantId,
      String asyncCallbackUrl,
      String sysPath,
      UserCredential userCredential)
      throws Exception {
    MemberWithdraw memberWithdraw = memberWithdrawService.getById(id);
    if (null == memberWithdraw) {
      throw new ServiceException("不存在的记录");
    }
    // 校验已受理出款是否允许其他人操作
    crossAccountCheck(userCredential, memberWithdraw);

    // 校验子账号当天取款审核额度
    SysUser sysUser = sysUserService.getByUsername(userCredential.getUsername());
    AdminLimitInfo adminLimitInfo =
        JsonUtils.parse(sysUser.getLimitInfo(), AdminLimitInfo.class);
    if (null != adminLimitInfo
        && StringUtils.equals(UserTypes.SUBUSER.value(), sysUser.getUserType())) {
      checkZzhWithdrawAmountAudit(
          adminLimitInfo,
          memberWithdraw.getCashMode(),
          memberWithdraw.getCashMoney(),
          userCredential.getUsername());
    }

    // 第三方代付商户
    PpMerchant ppMerchant = ppMerchantService.getById(ppMerchantId);
    if (!Optional.ofNullable(ppMerchant).isPresent()) {
      throw new ServiceException("请确认出款商户是否存在！");
    }

    // 代付接口信息
    PpInterface ppInterface = ppInterfaceService.get(ppMerchant.getPpInterfaceCode());
    verifyPpInterface(ppInterface);
    if (verifyPpMerchant(memberWithdraw, ppMerchant, ppInterface)) {
      throw new ServiceException("出款商户限制，请选择其他方式出款！");
    }

    // 封装第三方代付接口调用信息
    ProxyDispatchContext context = new ProxyDispatchContext();
    String asyncUrl = asyncCallbackUrl + "/api/proxyPay/onlineProxyPayAsyncCallback";
    context.setAsyncCallbackUrl(asyncUrl + "/" + memberWithdraw.getCashOrderNo());
    context.setSysPath(sysPath);
    // 设置会员真实姓名
    context.setName(memberWithdraw.getRealName());
    // 设置第三方接口信息
    fillProxyDispatchContext(context, ppInterface, memberWithdraw);
    // 设置银行编码,名称
    context.setBankCode(getProxyPayBankCode(memberWithdraw, ppMerchant, ppInterface));
    context.setBankName(memberWithdraw.getBankName());

    // 设置第三方商户参数
    context.setMerchantParameters(JSONObject.parseObject(ppMerchant.getParameters(), Map.class));

    // 设置客户请求地址
    context.setUserIpAddress(userCredential.getLoginIp());
    // 设置第三方商户出款数据
    buildWithdrawWithProxyMsg(memberWithdraw, ppMerchant, ppInterface);
    /** 第三方订单提交成功，将订单状态改为已出款 */
    memberWithdraw.setApproveReason(ppMerchant.getName() + "代付出款");
    memberWithdraw.setOperatorTime(new Date());
    memberWithdraw.setOperatorAccount(userCredential.getUsername());
    updateProxyWithdrawStatus(memberWithdraw, WithdrawStatus.SUCCESS.getValue());

    /** 请求第三方代付接口 */
    log.info("进入第三方出入款中心出款订单号: {}", memberWithdraw.getCashOrderNo());
    String resultStr =
        paymentCenterFeign.onlineProxyPay(
            context, memberWithdraw.getPpInterface(), memberWithdraw.getPpInterfaceName());
    if (!ProxyPayStatusEnum.PAY_SUCCESS.getName().equals(resultStr)) {
      throw new ServiceException("请求代付返回结果提示:" + resultStr + "！！！请立即联系第三方核实再出款！！！");
    }
    /** 不需要异步通知，直接将状态改成第三方出款完成 */
    if (0 != ppInterface.getAsynNotifyStatus()) {
      updateStatus(
          memberWithdraw,
          WithdrawStatus.SUCCESS.getValue(),
          ProxyPayStatusEnum.PAY_SUCCESS.getCode());
      /** 更新代付商户出款次数和金额 */
      updatePpMerchant(ppMerchant, memberWithdraw.getCashMoney());
      log.info("第三方出款成功,出款商户为:{} ,出款订单信息为：{}", memberWithdraw.getPpMerchantName(), memberWithdraw);
    }
  }

  @Override
  public ReturnMessage queryProxyOrder(Long id, Long ppMerchantId) throws Exception {
    MemberWithdraw memberWithdraw = memberWithdrawService.getById(id);
    if (memberWithdraw == null) {
      throw new ServiceException("不存在的记录");
    }
    // 第三方代付商户
    PpMerchant ppMerchant = ppMerchantService.getById(ppMerchantId);
    // 代付接口信息
    PpInterface ppInterface = ppInterfaceService.get(ppMerchant.getPpInterfaceCode());
    verifyPpInterface(ppInterface);
    // 封装第三方代付接口调用信息
    ProxyDispatchContext context = new ProxyDispatchContext();
    context.setName(ppInterface.getName());
    context.setVersion(ppInterface.getOrderQueryVersion());
    context.setCharset(ppInterface.getCharset());
    context.setDispatchType(ppInterface.getDispatchType());
    context.setDispatchUrl(ppInterface.getOrderQueryUrl());
    context.setDispatchMethod(ppInterface.getOrderQueryMethod());
    fillProxyDispatchContext(context, ppInterface, memberWithdraw);
    // 设置第三方商户参数
    context.setMerchantParameters(JSONObject.parseObject(ppMerchant.getParameters(), Map.class));
    // 设置银行编码,名称
    context.setBankCode(getProxyPayBankCode(memberWithdraw, ppMerchant, ppInterface));
    context.setBankName(memberWithdraw.getBankName());
    /** 请求第三方代付接口 */
    log.info("进入第三方出入款中心查询订单号: {}", memberWithdraw.getCashOrderNo());
    ReturnMessage a =
        paymentCenterFeign.onlineQueryProxyPay(
            context, ppInterface.getCode(), ppInterface.getName());
    return paymentCenterFeign.onlineQueryProxyPay(
        context, ppInterface.getCode(), ppInterface.getName());
  }

  @Override
  public String proxyPayAsyncCallback(
      String orderNo,
      String url,
      String method,
      List<NameValuePair> headers,
      String ipAddress,
      Map<String, String> callbackParameters,
      String requestBody)
      throws Exception {
    MemberWithdraw memberWithdraw =
        memberWithdrawService.lambdaQuery().eq(MemberWithdraw::getCashOrderNo, orderNo).one();
    /** 校验体现订单信息 */
    if (memberWithdraw == null) {
      throw new ServiceException("充值订单不存在或订单已处理");
    }
    String beanName = getProxyInterfaceCode(memberWithdraw);
    ProxyCallbackContext proxyCallbackContext = getProxyCallbackContent(memberWithdraw);
    proxyCallbackContext.setUrl(url);
    proxyCallbackContext.setMethod(method);
    proxyCallbackContext.setHeaders(headers);
    proxyCallbackContext.setIp(ipAddress);
    proxyCallbackContext.setCallbackParameters(callbackParameters);
    proxyCallbackContext.setRequestBody(requestBody);
    ProxyPayBackResult result =
        paymentCenterFeign.asyncCallbackProxyPay(
            proxyCallbackContext, beanName, memberWithdraw.getPpInterfaceName());
    if (memberWithdraw.getCashStatus() == WithdrawStatus.CANCELLED.getValue()
        || memberWithdraw.getCashStatus() == WithdrawStatus.REFUSE.getValue()
        || memberWithdraw.getCashStatus() == WithdrawStatus.SUCCESS.getValue()
        || null == memberWithdraw.getProxyPayStatus()
        || memberWithdraw.getProxyPayStatus() == ProxyPayStatusEnum.PAY_SUCCESS.getCode()) {
      log.info(
          "第三方出款订单"
              + memberWithdraw.getCashOrderNo()
              + "已经被处理了,响应第三方需要的信息:"
              + result.getResponseMsg());
      return result.getResponseMsg();
    }

    Member info = memberService.getById(memberWithdraw.getMemberId());
    if (info == null) {
      throw new ServiceException("用户不存在");
    }

    int orignCashStatus = memberWithdraw.getCashStatus();
    if (!result.isSuccess()) {
      /** 第三方出款失败，将代付状态改变 */
      memberWithdraw.setApproveReason("第三方出款失败");
      memberWithdraw.setProxyPayDesc("第三方出款失败");
      updateStatus(memberWithdraw, orignCashStatus, ProxyPayStatusEnum.PAY_FAIL.getCode());
      log.info("第三方出款订单 ：{} ！出款失败信息： {}", memberWithdraw.getCashOrderNo(), result.getMessge());
      return result.getResponseMsg();
    }

    /** 第三方出款成功 */
    memberWithdraw.setApproveReason("第三方出款成功");
    memberWithdraw.setProxyPayDesc("第三方出款成功");
    memberWithdraw.setProxyPayStatus(ProxyPayStatusEnum.PAY_SUCCESS.getCode());

    if (WithdrawStatus.SUCCESS.getValue() != orignCashStatus) {
      memberWithdraw.setCashStatus(WithdrawStatus.SUCCESS.getValue());
    }
    updateStatus(memberWithdraw, orignCashStatus, ProxyPayStatusEnum.PAY_SUCCESS.getCode());
    /** 更新代付商户出款次数和金额 */
    PpMerchant ppMerchant = ppMerchantService.getById(memberWithdraw.getPpMerchantId());
    updatePpMerchant(ppMerchant, memberWithdraw.getCashMoney());
    log.info("第三方出款成功,出款商户为:{} ,出款订单信息为：{}", memberWithdraw.getPpMerchantName(), memberWithdraw);
    return result.getResponseMsg();
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
                      LimitEnums.MEMBER_RECHARGE_LIMIT, MemberRechargeLimit.class))
              .orElseThrow(() -> new ServiceException("加载出入款配置信息失败，请联系客服！"));
      boolean toCheck =
          BooleanEnum.NO.match(limitInfo.getIsHandledAllowOthersOperate())
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
      MemberWithdraw memberWithdraw, PpMerchant ppMerchant, PpInterface ppInterface) {
    ProxyPayMerBean proxyPayMerBean = ProxyPayMerBean.conver2Bean(ppMerchant.getMerLimits());
    if (memberWithdraw.getCashMoney().compareTo(proxyPayMerBean.getMinLimitCash()) < 0
        || memberWithdraw.getCashMoney().compareTo(proxyPayMerBean.getMaxLimitCash()) > 0) {
      log.info("用户出款金额超出商户出款金额范围，过滤此商户，商户名称为：" + ppMerchant.getName());
      return true;
    }

    if (StringUtils.isNotEmpty(proxyPayMerBean.getUserLever())) {
      if (!StringUtils.contains(
          "," + proxyPayMerBean.getUserLever() + ",",
          String.format("%s" + memberWithdraw.getMemberLevel() + "%s", ",", ","))) {
        log.info("用户层级不在此代付商户设置的层级中，过滤此商户，商户名称为：" + ppMerchant.getName());
        return true;
      }
    }

    Map<String, String> banksMap =
        JSONObject.parseObject(
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
      log.info("代付商户不支持用户银行卡出款，过滤此商户，商户名称为：" + ppMerchant.getName());
      return true;
    }
    return false;
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

  private void fillProxyDispatchContext(
      ProxyDispatchContext context, PpInterface ppInterface, MemberWithdraw memberWithdraw) {
    context.setName(ppInterface.getName());
    context.setVersion(ppInterface.getVersion());
    context.setCharset(ppInterface.getCharset());
    context.setDispatchType(ppInterface.getDispatchType());
    context.setDispatchUrl(ppInterface.getDispatchUrl());
    context.setDispatchMethod(ppInterface.getDispatchMethod());
    context.setBankAccountNo(memberWithdraw.getBankCard());
    context.setBankCity(memberWithdraw.getBankAddress());
    context.setProxyAmount(memberWithdraw.getApproveMoney());
    context.setProxyOrderNo(memberWithdraw.getCashOrderNo());
    context.setOrderTime(memberWithdraw.getCreateTime());
    context.setUserAccount(memberWithdraw.getAccount());
  }

  private String getProxyPayBankCode(
      MemberWithdraw memberWithdraw, PpMerchant ppMerchant, PpInterface ppinterface) {
    if (null == ppMerchant || ppMerchant.getStatus() != SwitchStatusEnum.ENABLED.getValue()) {
      throw new ServiceException("第三方代付商户已关闭");
    }

    if (ppMerchant.getStatus() != SwitchStatusEnum.ENABLED.getValue()) {
      throw new ServiceException("第三方代付商户已关闭。");
    }

    String bankCode = "";
    String bankName = memberWithdraw.getBankName();
    Map<String, String> banksMap =
        JSONObject.parseObject(
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
      MemberWithdraw memberWithdraw, PpMerchant ppMerchant, PpInterface ppInterface) {
    memberWithdraw.setProxyPayDesc("第三方出款中。。。");
    memberWithdraw.setProxyPayStatus(ProxyPayStatusEnum.PAY_PROGRESS.getCode());
    memberWithdraw.setPpInterface(ppInterface.getCode());
    memberWithdraw.setPpInterfaceName(ppInterface.getName());
    memberWithdraw.setPpMerchantId(ppMerchant.getId());
    memberWithdraw.setPpMerchantName(ppMerchant.getName());
  }

  /**
   * 第三方出款成功，改变出款状态
   *
   * @param memberWithdraw
   */
  public void updateProxyWithdrawStatus(MemberWithdraw memberWithdraw, Integer cashStatus)
      throws Exception {
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
    if (!memberWithdrawService.update(updateMemberWithdraw)) {
      log.error(
          "修改提现订单异常：UserWithdraw="
              + memberWithdraw.toString()
              + ",origCashStatus="
              + memberWithdraw.getCashStatus());
      throw new ServiceException("订单已处理");
    }
    // 更新会员信息表
    memberInfoService.updateBalanceWithWithdraw(memberWithdraw.getMemberId(), memberWithdraw.getCashMoney());

    // 删除出款验证打码量记录的数据
    //    validWithdrawService.remove(userWithdraw.getUserId(), userWithdraw.getAddTime());
    //
    // 添加取现历史记录
    memberWithdraw.setCashStatus(cashStatus);
    MemberWithdrawHistory memberWithdrawHistory = new MemberWithdrawHistory();
    BeanUtils.copyProperties(memberWithdraw, memberWithdrawHistory);
    memberWithdrawHistoryService.save(memberWithdrawHistory);
  }

  private void updateStatus(
      MemberWithdraw memberWithdraw, Integer withdrawStatus, Integer proxyPayStatus) {
    boolean updateStatus =
        memberWithdrawService
            .lambdaUpdate()
            .set(
                ObjectUtils.isNotNull(memberWithdraw.getCashStatus()),
                MemberWithdraw::getCashStatus,
                memberWithdraw.getCashStatus())
            .set(MemberWithdraw::getApproveReason, memberWithdraw.getApproveReason())
            .set(MemberWithdraw::getProxyPayDesc, memberWithdraw.getProxyPayDesc())
            .set(MemberWithdraw::getProxyPayStatus, proxyPayStatus)
            .eq(MemberWithdraw::getId, memberWithdraw.getId())
            .eq(MemberWithdraw::getCashStatus, withdrawStatus)
            .update();
    if (!updateStatus) {
      log.error(
          "修改提现订单异常：MemberWithdraw="
              + memberWithdraw.toString()
              + ",origCashStatus="
              + withdrawStatus);
      throw new ServiceException("订单已处理");
    }
    /** 历史提现订单记录 */
    boolean updateHistoryStatus =
        memberWithdrawHistoryService
            .lambdaUpdate()
            .set(
                ObjectUtils.isNotNull(memberWithdraw.getCashStatus()),
                MemberWithdrawHistory::getCashStatus,
                memberWithdraw.getCashStatus())
            .set(MemberWithdrawHistory::getApproveReason, memberWithdraw.getApproveReason())
            .set(MemberWithdrawHistory::getProxyPayDesc, memberWithdraw.getProxyPayDesc())
            .set(MemberWithdrawHistory::getProxyPayStatus, proxyPayStatus)
            .eq(MemberWithdrawHistory::getId, memberWithdraw.getId())
            .eq(MemberWithdrawHistory::getCashStatus, withdrawStatus)
            .update();
    if (!updateHistoryStatus) {
      log.error(
          "修改提现订单异常：UserWithdraw="
              + memberWithdraw.toString()
              + ",origCashStatus="
              + withdrawStatus);
      throw new ServiceException("历史订单不存在");
    }
  }

  private void updatePpMerchant(PpMerchant ppMerchant, BigDecimal cashMomey) {
    boolean updateMerchant =
        ppMerchantService
            .lambdaUpdate()
            .set(PpMerchant::getProxyTimes, ppMerchant.getProxyTimes() + 1)
            .set(PpMerchant::getProxyAmount, ppMerchant.getProxyAmount().add(cashMomey))
            .eq(PpMerchant::getId, ppMerchant.getId())
            .update();
    if (!updateMerchant) {
      log.error("修改代付商户出款次数和金额异常：ppMerchant=" + ppMerchant.toString());
      throw new ServiceException("修改代付商户出款次数和金额异常");
    }
  }

  private String getProxyInterfaceCode(MemberWithdraw memberWithdraw) throws Exception {
    PpInterface ppInterface = ppInterfaceService.get(memberWithdraw.getPpInterface());
    if (ppInterface == null || StringUtils.isEmpty(ppInterface.getCode())) {
      throw new Exception("第三方代付接口已关闭");
    }
    return ppInterface.getCode();
  }

  private ProxyCallbackContext getProxyCallbackContent(MemberWithdraw memberWithdraw)
      throws Exception {

    PpInterface ppInterface = ppInterfaceService.get(memberWithdraw.getPpInterface());
    if (ppInterface == null) {
      throw new Exception("第三方代付接口已关闭");
    }
    PpMerchant ppMerchant = ppMerchantService.getById(memberWithdraw.getPpMerchantId());
    if (ppMerchant == null) {
      throw new Exception("第三方代付商户已关闭");
    }

    ProxyCallbackContext context = new ProxyCallbackContext();
    context.setName(ppInterface.getName());
    context.setCashOrderNo(memberWithdraw.getCashOrderNo());
    context.setCharset(ppInterface.getCharset());
    context.setVersion(ppInterface.getVersion());
    context.setProxyAmount(memberWithdraw.getCashMoney());
    context.setMerchantParameters(JSONObject.parseObject(ppMerchant.getParameters(), Map.class));
    return context;
  }
}
