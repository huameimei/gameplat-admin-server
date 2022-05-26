package com.gameplat.admin.interceptor;

import cn.hutool.core.util.ObjectUtil;
import com.gameplat.admin.service.*;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.common.enums.BooleanEnum;
import com.gameplat.common.enums.LimitEnums;
import com.gameplat.common.enums.WithdrawStatus;
import com.gameplat.common.model.bean.limit.AdminLoginLimit;
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

  @Autowired private LimitInfoService limitService;

  @Autowired private TwoFactorAuthenticationService twoFactorAuthenticationService;

  @Override
  public boolean preHandle(
          @NotNull HttpServletRequest request,
          @NotNull HttpServletResponse response,
          @NotNull Object handler) {

    if (rwLimitVerify(request)) {
      AdminLoginLimit adminLoginLimit = limitService.get(LimitEnums.ADMIN_LOGIN_CONFIG);
      log.info("后台账号验证限制：{}", adminLoginLimit);
      // 验证验证码
      this.checkCaptchaCode(adminLoginLimit.getGoogleAuthSwitch(), request);
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
