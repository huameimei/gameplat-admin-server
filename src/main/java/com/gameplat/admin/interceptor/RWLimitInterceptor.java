package com.gameplat.admin.interceptor;

import cn.hutool.core.util.ObjectUtil;
import com.gameplat.admin.service.*;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.common.enums.BooleanEnum;
import com.gameplat.common.enums.LimitEnums;
import com.gameplat.common.enums.WithdrawStatus;
import com.gameplat.common.model.bean.limit.MemberRechargeLimit;
import com.gameplat.common.model.bean.limit.MemberWithdrawLimit;
import com.gameplat.common.util.Convert;
import com.gameplat.security.SecurityUserHolder;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录拦截器
 *
 * @author robben
 */
@Log4j2
public class RWLimitInterceptor implements HandlerInterceptor {

  /**
   * 提现接口
   */
  private final static String WITH_URL = "api/admin/finance/memberWithdraw/modifyCashStatus";


  /**
   * 单个充值入款
   */
  private final static String RECH_URIL = "/api/admin/finance/rechargeOrder/accept";

  /**
   * 批量充值入款
   */
  private final static String RECH_BATCH_URIL = "/api/admin/finance/rechargeOrder/batchAccept";

  @Autowired private LimitInfoService limitService;

  @Autowired private TwoFactorAuthenticationService twoFactorAuthenticationService;

  @Override
  public boolean preHandle(
          @NotNull HttpServletRequest request,
          @NotNull HttpServletResponse response,
          @NotNull Object handler) {

    //判断是否是充值谷歌限制
    if (request.getRequestURI().contains(RECH_URIL) || request.getRequestURI().contains(RECH_BATCH_URIL)) {
      MemberRechargeLimit memberRechargeLimit = limitService.get(LimitEnums.MEMBER_RECHARGE_LIMIT);
      checkCaptchaCode(memberRechargeLimit.getRechGooGleLimit(), request);
    } else {
      //判断是否是提现谷歌限制
      if (rwLimitVerify(request)) {
        MemberWithdrawLimit memberWithdrawLimit = limitService.get(LimitEnums.MEMBER_WITHDRAW_LIMIT);
        checkCaptchaCode(memberWithdrawLimit.getWithGooGleLimit(), request);
      }
    }
    return true;
  }

  private boolean rwLimitVerify(HttpServletRequest request) {
    boolean flag = true;

    String requestURI = request.getRequestURI();
    if (requestURI.contains(WITH_URL)) {
      String cashStatus = request.getParameter("cashStatus");
      if (ObjectUtil.isNull(cashStatus)) {
        throw new ServiceException("提现状态不能为空！");
      }
      if (WithdrawStatus.SUCCESS.getValue() != Convert.toInt(cashStatus)) {
        flag = false;
      }
    }
    return flag;
  }

  /**
   * 验证验证码
   *
   * @param loginCaptchaSwitch 是否开启了登录验证码
   * @param request HttpServletRequest
   */
  private void checkCaptchaCode(int loginCaptchaSwitch, HttpServletRequest request) {
    if (BooleanEnum.YES.match(loginCaptchaSwitch)) {
      String code = request.getParameter("code");
      // 谷歌认证
      twoFactorAuthenticationService.verify2Fa(SecurityUserHolder.getUserId(), code);
    }
  }
}
