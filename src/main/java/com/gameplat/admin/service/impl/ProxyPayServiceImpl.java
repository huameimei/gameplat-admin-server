package com.gameplat.admin.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gameplat.admin.enums.CashEnum;
import com.gameplat.admin.enums.ProxyPayStatusEnum;
import com.gameplat.admin.feign.PaymentCenterFeign;
import com.gameplat.admin.model.bean.AdminLimitInfo;
import com.gameplat.admin.model.bean.ProxyPayMerBean;
import com.gameplat.admin.model.bean.ReturnMessage;
import com.gameplat.admin.model.vo.MemberWithdrawBankVo;
import com.gameplat.admin.service.*;
import com.gameplat.admin.util.MoneyUtils;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.json.JsonUtils;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.base.common.web.Result;
import com.gameplat.basepay.pay.bean.NameValuePair;
import com.gameplat.basepay.proxypay.thirdparty.ProxyCallbackContext;
import com.gameplat.basepay.proxypay.thirdparty.ProxyDispatchContext;
import com.gameplat.basepay.proxypay.thirdparty.ProxyPayBackResult;
import com.gameplat.common.constant.WithdrawTypeConstant;
import com.gameplat.common.enums.*;
import com.gameplat.common.model.bean.limit.MemberRechargeLimit;
import com.gameplat.common.model.bean.limit.MemberWithdrawLimit;
import com.gameplat.model.entity.member.Member;
import com.gameplat.model.entity.member.MemberInfo;
import com.gameplat.model.entity.member.MemberWithdraw;
import com.gameplat.model.entity.member.MemberWithdrawHistory;
import com.gameplat.model.entity.pay.PpInterface;
import com.gameplat.model.entity.pay.PpMerchant;
import com.gameplat.model.entity.sys.SysUser;
import com.gameplat.security.context.UserCredential;
import lombok.extern.slf4j.Slf4j;
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

  @Autowired private ValidWithdrawService validWithdrawService;

  @Autowired private MemberRwReportService memberRwReportService;

  private static boolean verifyPpMerchant(
      MemberWithdraw memberWithdraw, PpMerchant ppMerchant, PpInterface ppInterface) {
    ProxyPayMerBean proxyPayMerBean = ProxyPayMerBean.conver2Bean(ppMerchant.getMerLimits());
    if (memberWithdraw.getCashMoney().compareTo(proxyPayMerBean.getMinLimitCash()) < 0
        || memberWithdraw.getCashMoney().compareTo(proxyPayMerBean.getMaxLimitCash()) > 0) {
      log.info("???????????????????????????????????????????????????????????????????????????????????????" + ppMerchant.getName());
      return true;
    }

    if (StringUtils.isNotEmpty(proxyPayMerBean.getUserLever())) {
      if (!StringUtils.contains(
          "," + proxyPayMerBean.getUserLever() + ",",
          String.format("%s" + memberWithdraw.getMemberLevel() + "%s", ",", ","))) {
        log.info("??????????????????????????????????????????????????????????????????????????????????????????" + ppMerchant.getName());
        return true;
      }
    }
    List<MemberWithdrawBankVo> bankVoList =
            JSONUtil.toList(
                    (JSONArray) JSONUtil.parseObj(ppInterface.getLimtInfo()).get("banks"),
                    MemberWithdrawBankVo.class);
    // ????????????????????????
    /** ???????????????????????? */
    boolean isBankName = true;
    if (!WithdrawTypeConstant.BANK.equals(memberWithdraw.getWithdrawType())) {
      isBankName = false;
    }
    for (MemberWithdrawBankVo ex : bankVoList) {
      if (StringUtils.contains(ex.getName(), memberWithdraw.getBankName())
              || StringUtils.contains(memberWithdraw.getBankName(), ex.getName())) {
        isBankName = false;
        break;
      }
      if (StringUtils.contains(ex.getName(), "??????")
              && StringUtils.contains(memberWithdraw.getBankName(), "??????")) {
        isBankName = false;
        break;
      }
    }
    if (isBankName) {
      log.info("?????????????????????????????????????????????????????????????????????????????????" + ppMerchant.getName());
      return true;
    }
    return false;
  }

  @Override
  public void proxyPay(
      Long id,
      Long ppMerchantId,
      String asyncCallbackUrl,
      String sysPath,
      UserCredential userCredential)
      throws Exception {
    MemberWithdraw memberWithdraw = memberWithdrawService.getById(id);
    log.info("???????????????????????????==={}", memberWithdraw.getCashOrderNo());
    if (null == memberWithdraw) {
      throw new ServiceException("??????????????????");
    }

    Member member = memberService.getById(memberWithdraw.getMemberId());
    if (member == null) {
      throw new ServiceException("???????????????");
    }

    MemberInfo memberInfo = memberInfoService.getById(memberWithdraw.getMemberId());
    if (memberInfo == null) {
      throw new ServiceException("???????????????????????????");
    }

    // ????????????????????????????????????????????????
    crossAccountCheck(userCredential, memberWithdraw);

    // ???????????????????????????????????????
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

    // ?????????????????????
    PpMerchant ppMerchant = ppMerchantService.getById(ppMerchantId);
    if (!Optional.ofNullable(ppMerchant).isPresent()) {
      throw new ServiceException("????????????????????????????????????");
    }

    // ??????????????????
    PpInterface ppInterface = ppInterfaceService.get(ppMerchant.getPpInterfaceCode());
    verifyPpInterface(ppInterface);
    if (verifyPpMerchant(memberWithdraw, ppMerchant, ppInterface)) {
      throw new ServiceException("???????????????????????????????????????????????????");
    }

    // ???????????????????????????????????????
    ProxyDispatchContext context = new ProxyDispatchContext();
    MemberWithdrawLimit withdrawLimit = this.limitInfoService.get(LimitEnums.MEMBER_WITHDRAW_LIMIT);
    String callbackDomain = withdrawLimit.getCallbackDomain();
    String domain = StringUtils.isNotEmpty(callbackDomain) ? callbackDomain : asyncCallbackUrl;
    if(!domain.endsWith("/")){
      domain += "/";
    }
    String asyncUrl =
            domain + "api/admin/callback/proxyPayAsyncCallback";
    String syncUrl =
            domain + "api/admin/callback/fixedProxyPayAsyncCallback";
    context.setAsyncCallbackUrl(asyncUrl + "/" + memberWithdraw.getCashOrderNo());
    // ??????????????????
    context.setSyncCallbackUrl(syncUrl + "/" + ppMerchant.getPpInterfaceCode());
    context.setSysPath(sysPath);
    // ???????????????????????????
    fillProxyDispatchContext(context, ppInterface, memberWithdraw);
    // ??????????????????,??????
    context.setBankCode(getProxyPayBankCode(memberWithdraw, ppMerchant, ppInterface));
    context.setBankName(memberWithdraw.getBankName());
    // ????????????????????????????????????
    context.setCurrencyRate(memberWithdraw.getCurrencyRate());
    context.setCurrencyCount(memberWithdraw.getCurrencyCount());
    // ???????????????????????????
    context.setMerchantParameters(JSONObject.parseObject(ppMerchant.getParameters(), Map.class));

    // ????????????????????????
    context.setUserIpAddress(userCredential.getLoginIp());
    // ?????????????????????????????????
    buildWithdrawWithProxyMsg(memberWithdraw, ppMerchant, ppInterface);
    /** ???????????????????????????????????????????????????????????? */
    memberWithdraw.setApproveReason(ppMerchant.getName() + "????????????");
    memberWithdraw.setOperatorTime(new Date());
    memberWithdraw.setOperatorAccount(userCredential.getUsername());
    updateProxyWithdrawStatus(
        memberWithdraw, WithdrawStatus.SUCCESS.getValue(), member, memberInfo);

    /** ??????????????????????????? */
    log.info("?????????????????????????????????????????????: {}", memberWithdraw.getCashOrderNo());
    String resultStr =
        paymentCenterFeign.onlineProxyPay(
            context, memberWithdraw.getPpInterface(), memberWithdraw.getPpInterfaceName());
    log.info("????????????????????????{}", resultStr);
    Result<ProxyPayBackResult> result = JSONUtil.toBean(resultStr, Result.class);
    if (!Objects.equals(ProxyPayStatusEnum.PAY_SUCCESS.getName(), result.getData())) {
      throw new ServiceException("???????????????????????????????????????????????????????????????????????????");
    }
    /*if (!result.isSucceed() || 0 != result.getCode()) {
      throw new ServiceException("??????????????????????????????:" + result.getMessage() + "?????????????????????????????????????????????????????????");
    }
    ProxyPayBackResult proxyPayBackResult = result.getData();
    *//** ????????????????????????????????????????????? *//*
    if (null != proxyPayBackResult && null != proxyPayBackResult.getApproveCurrencyRate()
        || null != proxyPayBackResult.getApproveCurrencyCount()) {
      memberWithdrawService
          .lambdaUpdate()
          .set(MemberWithdraw::getApproveCurrencyRate, proxyPayBackResult.getApproveCurrencyRate())
          .set(
              MemberWithdraw::getApproveCurrencyCount, proxyPayBackResult.getApproveCurrencyCount())
          .eq(MemberWithdraw::getId, memberWithdraw.getId())
          .update();
    }*/
    memberWithdrawService.updateById(memberWithdraw);

    // ??????????????????????????????
    memberInfoService.updateFreeze(memberInfo.getMemberId(), memberWithdraw.getCashMoney().negate());
    // ?????????????????????????????????
    memberInfoService.updateUserWithTimes(
            memberInfo.getMemberId(), memberWithdraw.getCashMoney().negate(), memberWithdraw.getPointFlag());

    /** ?????????????????????????????????????????????????????????????????? */
    if (0 != ppInterface.getAsynNotifyStatus()) {
      updateStatus(
          memberWithdraw,
          WithdrawStatus.SUCCESS.getValue(),
          ProxyPayStatusEnum.PAY_SUCCESS.getCode());
      /** ??????????????????????????????????????? */
      updatePpMerchant(ppMerchant, memberWithdraw.getCashMoney());
      log.info("?????????????????????,???????????????:{} ,????????????????????????{}", memberWithdraw.getPpMerchantName(), memberWithdraw);
    }
  }

  @Override
  public ReturnMessage queryProxyOrder(Long id, Long ppMerchantId) throws Exception {
    MemberWithdraw memberWithdraw = memberWithdrawService.getById(id);
    if (memberWithdraw == null) {
      throw new ServiceException("??????????????????");
    }
    // ?????????????????????
    PpMerchant ppMerchant = ppMerchantService.getById(ppMerchantId);
    // ??????????????????
    PpInterface ppInterface = ppInterfaceService.get(ppMerchant.getPpInterfaceCode());
    verifyPpInterface(ppInterface);
    // ???????????????????????????????????????
    ProxyDispatchContext context = new ProxyDispatchContext();
    context.setName(ppInterface.getName());
    context.setVersion(ppInterface.getOrderQueryVersion());
    context.setCharset(ppInterface.getCharset());
    context.setDispatchType(ppInterface.getDispatchType());
    context.setDispatchUrl(ppInterface.getOrderQueryUrl());
    context.setDispatchMethod(ppInterface.getOrderQueryMethod());
    fillProxyDispatchContext(context, ppInterface, memberWithdraw);
    // ???????????????????????????
    context.setMerchantParameters(JSONObject.parseObject(ppMerchant.getParameters(), Map.class));
    // ??????????????????,??????
    context.setBankCode(getProxyPayBankCode(memberWithdraw, ppMerchant, ppInterface));
    context.setBankName(memberWithdraw.getBankName());
    /** ??????????????????????????? */
    log.info("?????????????????????????????????????????????: {}", memberWithdraw.getCashOrderNo());
    String resultStr =
        paymentCenterFeign.onlineQueryProxyPay(
            context, ppInterface.getCode(), ppInterface.getName());
    Result<ReturnMessage> result = JSONUtil.toBean(resultStr, Result.class);
    if (!result.isSucceed() || 0 != result.getCode()) {
      throw new ServiceException("???????????????????????????:" + result.getMessage() + "?????????????????????????????????????????????????????????");
    }
    return result.getData();
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
    log.info("?????????????????????={}", orderNo);
    MemberWithdraw memberWithdraw =
        memberWithdrawService.lambdaQuery().eq(MemberWithdraw::getCashOrderNo, orderNo).one();
    /** ???????????????????????? */
    if (memberWithdraw == null) {
      throw new ServiceException("???????????????????????????????????????");
    }
    String beanName = getProxyInterfaceCode(memberWithdraw);
    ProxyCallbackContext proxyCallbackContext = getProxyCallbackContent(memberWithdraw);
    proxyCallbackContext.setUrl(url);
    proxyCallbackContext.setMethod(method);
    proxyCallbackContext.setHeaders(headers);
    proxyCallbackContext.setIp(ipAddress);
    proxyCallbackContext.setCallbackParameters(callbackParameters);
    proxyCallbackContext.setRequestBody(requestBody);
    String resultStr =
        paymentCenterFeign.asyncCallbackProxyPay(
            proxyCallbackContext, beanName, memberWithdraw.getPpInterfaceName());
    log.info("??????????????????????????????{}", resultStr);
    Result result = JSONUtil.toBean(resultStr, Result.class);
    ProxyPayBackResult proxyPayBackResult = JSONObject.parseObject(JSONObject.toJSONString(result.getData()), ProxyPayBackResult.class);
    if (!result.isSucceed() || 0 != result.getCode()) {
      throw new ServiceException("?????????????????????????????????:" + proxyPayBackResult.getMessage() + "?????????????????????????????????????????????????????????");
    }
    if (memberWithdraw.getCashStatus() == WithdrawStatus.CANCELLED.getValue()
        || memberWithdraw.getCashStatus() == WithdrawStatus.REFUSE.getValue()
        || memberWithdraw.getCashStatus() == WithdrawStatus.SUCCESS.getValue()
        || null == memberWithdraw.getProxyPayStatus()
        || memberWithdraw.getProxyPayStatus() == ProxyPayStatusEnum.PAY_SUCCESS.getCode()) {
      log.info(
          "?????????????????????"
              + memberWithdraw.getCashOrderNo()
              + "??????????????????,??????????????????????????????:"
              + proxyPayBackResult.getResponseMsg());
      return proxyPayBackResult.getResponseMsg();
    }

    Member info = memberService.getById(memberWithdraw.getMemberId());
    if (info == null) {
      throw new ServiceException("???????????????");
    }

    int orignCashStatus = memberWithdraw.getCashStatus();
    if (!proxyPayBackResult.isSuccess()) {
      /** ????????????????????????????????????????????? */
      memberWithdraw.setApproveReason("?????????????????????");
      memberWithdraw.setProxyPayDesc("?????????????????????");
      updateStatus(memberWithdraw, orignCashStatus, ProxyPayStatusEnum.PAY_FAIL.getCode());
      log.info("????????????????????? ???{} ???????????????????????? {}", memberWithdraw.getCashOrderNo(), result.getMessage());
      return proxyPayBackResult.getResponseMsg();
    }

    /** ????????????????????? */
    memberWithdraw.setApproveReason("?????????????????????");
    memberWithdraw.setProxyPayDesc("?????????????????????");
    memberWithdraw.setProxyPayStatus(ProxyPayStatusEnum.PAY_SUCCESS.getCode());

    if (WithdrawStatus.SUCCESS.getValue() != orignCashStatus) {
      memberWithdraw.setCashStatus(WithdrawStatus.SUCCESS.getValue());
    }
    updateStatus(memberWithdraw, orignCashStatus, ProxyPayStatusEnum.PAY_SUCCESS.getCode());
    /** ??????????????????????????????????????? */
    PpMerchant ppMerchant = ppMerchantService.getById(memberWithdraw.getPpMerchantId());
    updatePpMerchant(ppMerchant, memberWithdraw.getCashMoney());
    log.info("?????????????????????,???????????????:{} ,????????????????????????{}", memberWithdraw.getPpMerchantName(), memberWithdraw);
    return proxyPayBackResult.getResponseMsg();
  }

  /**
   * ??????????????????????????????????????????
   * ????????????syncCallbackUrl????????????????????????
   * ????????????ppOrderNo????????????????????????
   * */
  @Override
  public String fixedProxyPayAsyncCallback(
      String interfaceCode,
      String url,
      String method,
      List<NameValuePair> headers,
      String ipAddress,
      Map<String, String> callbackParameters,
      String requestBody)
      throws Exception {
    log.info("??????????????????????????????==={}", interfaceCode);
    PpInterface ppInterface = ppInterfaceService.get(interfaceCode);
    if (ppInterface == null) {
      throw new Exception("??????????????????????????????");
    }
    List<PpMerchant> list =
        ppMerchantService.lambdaQuery().eq(PpMerchant::getPpInterfaceCode, interfaceCode).list();
    if (CollectionUtils.isEmpty(list)) {
      throw new Exception("??????????????????????????????");
    }
    PpMerchant ppMerchant = list.get(0);
    ProxyCallbackContext proxyCallbackContext = new ProxyCallbackContext();
    proxyCallbackContext.setUrl(url);
    proxyCallbackContext.setMethod(method);
    proxyCallbackContext.setHeaders(headers);
    proxyCallbackContext.setIp(ipAddress);
    proxyCallbackContext.setCallbackParameters(callbackParameters);
    proxyCallbackContext.setRequestBody(requestBody);
    proxyCallbackContext.setMerchantParameters(JSONObject.parseObject(ppMerchant.getParameters(), Map.class));
    String resultStr =
        paymentCenterFeign.asyncCallbackProxyPay(
            proxyCallbackContext, interfaceCode, ppMerchant.getName());
    log.info("??????????????????????????????{}", resultStr);
    Result result = JSONUtil.toBean(resultStr, Result.class);
    ProxyPayBackResult proxyPayBackResult = JSONObject.parseObject(JSONObject.toJSONString(result.getData()), ProxyPayBackResult.class);
    if (!result.isSucceed() || 0 != result.getCode()) {
      throw new ServiceException(
          "?????????????????????????????????:" + proxyPayBackResult.getMessage() + "?????????????????????????????????????????????????????????");
    }
    String ppOrderNo = proxyPayBackResult.getPpOrderNo();
    if (StringUtils.isEmpty(ppOrderNo)) {
      throw new ServiceException("???????????????????????????????????????");
    }
    log.info("?????????????????????==={}", ppOrderNo);
    MemberWithdraw memberWithdraw =
        memberWithdrawService.lambdaQuery().eq(MemberWithdraw::getCashOrderNo, ppOrderNo).one();
    /** ???????????????????????? */
    if (memberWithdraw == null) {
      throw new ServiceException("???????????????????????????????????????");
    }
    /*String beanName = getProxyInterfaceCode(memberWithdraw);
    proxyCallbackContext = getProxyCallbackContent(memberWithdraw);*/
    if (memberWithdraw.getCashStatus() == WithdrawStatus.CANCELLED.getValue()
        || memberWithdraw.getCashStatus() == WithdrawStatus.REFUSE.getValue()
        || memberWithdraw.getCashStatus() == WithdrawStatus.SUCCESS.getValue()
        || null == memberWithdraw.getProxyPayStatus()
        || memberWithdraw.getProxyPayStatus() == ProxyPayStatusEnum.PAY_SUCCESS.getCode()) {
      log.info(
          "?????????????????????"
              + memberWithdraw.getCashOrderNo()
              + "??????????????????,??????????????????????????????:"
              + proxyPayBackResult.getResponseMsg());
      return proxyPayBackResult.getResponseMsg();
    }

    Member info = memberService.getById(memberWithdraw.getMemberId());
    if (info == null) {
      throw new ServiceException("???????????????");
    }

    int orignCashStatus = memberWithdraw.getCashStatus();
    if (!proxyPayBackResult.isSuccess()) {
      /** ????????????????????????????????????????????? */
      memberWithdraw.setApproveReason("?????????????????????");
      memberWithdraw.setProxyPayDesc("?????????????????????");
      updateStatus(memberWithdraw, orignCashStatus, ProxyPayStatusEnum.PAY_FAIL.getCode());
      log.info("????????????????????? ???{} ???????????????????????? {}", memberWithdraw.getCashOrderNo(), result.getMessage());
      return proxyPayBackResult.getResponseMsg();
    }

    /** ????????????????????? */
    memberWithdraw.setApproveReason("?????????????????????");
    memberWithdraw.setProxyPayDesc("?????????????????????");
    memberWithdraw.setProxyPayStatus(ProxyPayStatusEnum.PAY_SUCCESS.getCode());

    if (WithdrawStatus.SUCCESS.getValue() != orignCashStatus) {
      memberWithdraw.setCashStatus(WithdrawStatus.SUCCESS.getValue());
    }
    updateStatus(memberWithdraw, orignCashStatus, ProxyPayStatusEnum.PAY_SUCCESS.getCode());
    /** ??????????????????????????????????????? */
    ppMerchant = ppMerchantService.getById(memberWithdraw.getPpMerchantId());
    updatePpMerchant(ppMerchant, memberWithdraw.getCashMoney());
    log.info("?????????????????????,???????????????:{} ,????????????????????????{}", memberWithdraw.getPpMerchantName(), memberWithdraw);
    return proxyPayBackResult.getResponseMsg();
  }

  /** ????????????????????????????????????????????????????????? ??????????????????????????????????????? */
  private void crossAccountCheck(UserCredential userCredential, MemberWithdraw memberWithdraw)
      throws ServiceException {
    if (userCredential != null
        && StringUtils.isNotEmpty(userCredential.getUsername())
        && null != memberWithdraw) {
      MemberRechargeLimit limitInfo = limitInfoService.getRechargeLimit();
      boolean toCheck =
          BooleanEnum.NO.match(limitInfo.getIsHandledAllowOthersOperate())
              && !userCredential.isSuperAdmin();
      if (toCheck) {
        if (!Objects.equals(WithdrawStatus.UNHANDLED.getValue(), memberWithdraw.getCashStatus())
                && !StringUtils.equalsIgnoreCase(
                userCredential.getUsername(), memberWithdraw.getAcceptAccount())) {
          throw new ServiceException("????????????????????????:" + memberWithdraw.getCashOrderNo());
        }
      }
    }
  }

  private void verifyPpInterface(PpInterface ppInterface) {
    if (ppInterface == null) {
      throw new ServiceException("?????????????????????????????????");
    }
    if (ppInterface.getStatus() != SwitchStatusEnum.ENABLED.getValue()) {
      throw new ServiceException("?????????????????????????????????");
    }
  }

  /**
   * ?????????????????????????????????
   *
   * @param adminLimitInfo AdminLimitInfo
   * @param cashMode Integer
   */
  public void checkZzhWithdrawAmountAudit(
      AdminLimitInfo adminLimitInfo, Integer cashMode, BigDecimal cashMoney, String userName) {
    if (cashMode == CashEnum.CASH_MODE_USER.getValue()) {
      if (adminLimitInfo.getMaxWithdrawAmount().compareTo(BigDecimal.ZERO) > 0
          && cashMoney.compareTo(adminLimitInfo.getMaxWithdrawAmount()) > 0) {
        String buffer =
            userName
                + "???????????????????????????????????????????????????"
                + adminLimitInfo.getMaxWithdrawAmount()
                + "?????? ????????????"
                + MoneyUtils.toYuanStr(cashMoney.subtract(adminLimitInfo.getMaxWithdrawAmount()))
                + "???";
        throw new ServiceException(buffer);
      }
    } else if (cashMode == CashEnum.CASH_MODE_HAND.getValue()) {
      if (adminLimitInfo.getMaxManualWithdrawAmount().compareTo(BigDecimal.ZERO) > 0
          && cashMoney.compareTo(adminLimitInfo.getMaxManualWithdrawAmount()) > 0) {
        String buffer =
            userName
                + "??????????????????????????????????????????????????????"
                + adminLimitInfo.getMaxManualWithdrawAmount()
                + "?????? ????????????"
                + MoneyUtils.toYuanStr(
                    cashMoney.subtract(adminLimitInfo.getMaxManualWithdrawAmount()))
                + "???";
        throw new ServiceException(buffer);
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
    if(StringUtils.isNotEmpty(memberWithdraw.getRealName())){
      context.setUserRealName(memberWithdraw.getRealName());
    }
  }

  private String getProxyPayBankCode(
      MemberWithdraw memberWithdraw, PpMerchant ppMerchant, PpInterface ppinterface) {
    if (null == ppMerchant || !SwitchStatusEnum.ENABLED.match(ppMerchant.getStatus())) {
      throw new ServiceException("??????????????????????????????");
    }

    String bankCode = "";
    List<MemberWithdrawBankVo> bankVoList =
            JSONUtil.toList(
                    (JSONArray) JSONUtil.parseObj(ppinterface.getLimtInfo()).get("banks"),
                    MemberWithdrawBankVo.class);
    for (MemberWithdrawBankVo ex : bankVoList) {
      if (StringUtils.contains(ex.getName(), memberWithdraw.getBankName())
              || StringUtils.contains(memberWithdraw.getBankName(), ex.getName())) {
        bankCode = ex.getCode();
        break;
      }
      if (StringUtils.contains(ex.getName(), "??????")
              && StringUtils.contains(memberWithdraw.getBankName(), "??????")) {
        bankCode = ex.getCode();
        break;
      }
      if (StringUtils.contains(ex.getName().toLowerCase(), memberWithdraw.getWithdrawType().toLowerCase())
              || StringUtils.contains(memberWithdraw.getWithdrawType().toLowerCase(), ex.getName().toLowerCase())) {
        bankCode = ex.getCode();
        break;
      }
    }

    if (StringUtils.isBlank(bankCode)) {
      throw new ServiceException("???????????????????????????????????????????????????");
    }
    return bankCode;
  }

  private void buildWithdrawWithProxyMsg(
      MemberWithdraw memberWithdraw, PpMerchant ppMerchant, PpInterface ppInterface) {
    memberWithdraw.setProxyPayDesc("???????????????????????????");
    memberWithdraw.setProxyPayStatus(ProxyPayStatusEnum.PAY_PROGRESS.getCode());
    memberWithdraw.setPpInterface(ppInterface.getCode());
    memberWithdraw.setPpInterfaceName(ppInterface.getName());
    memberWithdraw.setPpMerchantId(ppMerchant.getId());
    memberWithdraw.setPpMerchantName(ppMerchant.getName());
  }

  /**
   * ??????????????????????????????????????????
   *
   * @param memberWithdraw
   */
  public void updateProxyWithdrawStatus(
      MemberWithdraw memberWithdraw, Integer cashStatus, Member member, MemberInfo memberInfo)
      throws Exception {
    // ??????????????????
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
          "???????????????????????????UserWithdraw="
              + memberWithdraw.toString()
              + ",origCashStatus="
              + memberWithdraw.getCashStatus());
      throw new ServiceException("???????????????");
    }

    if (!UserTypes.PROMOTION.value().equals(member.getUserType())) {
      memberRwReportService.addWithdraw(member, memberInfo.getTotalWithdrawTimes(), memberWithdraw);
    }

    // ??????????????????????????????????????????
    validWithdrawService.remove(memberWithdraw.getMemberId(), memberWithdraw.getCreateTime());

    // ????????????????????????
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
          "???????????????????????????MemberWithdraw="
              + memberWithdraw.toString()
              + ",origCashStatus="
              + withdrawStatus);
      throw new ServiceException("???????????????");
    }
    /** ???????????????????????? */
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
          "???????????????????????????UserWithdraw="
              + memberWithdraw.toString()
              + ",origCashStatus="
              + withdrawStatus);
      throw new ServiceException("?????????????????????");
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
      log.error("????????????????????????????????????????????????ppMerchant=" + ppMerchant.toString());
      throw new ServiceException("?????????????????????????????????????????????");
    }
  }

  private String getProxyInterfaceCode(MemberWithdraw memberWithdraw) throws Exception {
    PpInterface ppInterface = ppInterfaceService.get(memberWithdraw.getPpInterface());
    if (ppInterface == null || StringUtils.isEmpty(ppInterface.getCode())) {
      throw new Exception("??????????????????????????????");
    }
    return ppInterface.getCode();
  }

  private ProxyCallbackContext getProxyCallbackContent(MemberWithdraw memberWithdraw)
      throws Exception {

    PpInterface ppInterface = ppInterfaceService.get(memberWithdraw.getPpInterface());
    if (ppInterface == null) {
      throw new Exception("??????????????????????????????");
    }
    PpMerchant ppMerchant = ppMerchantService.getById(memberWithdraw.getPpMerchantId());
    if (ppMerchant == null) {
      throw new Exception("??????????????????????????????");
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
