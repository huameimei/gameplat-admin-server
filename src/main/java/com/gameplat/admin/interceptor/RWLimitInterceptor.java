package com.gameplat.admin.interceptor;

import com.gameplat.admin.service.*;
import com.gameplat.common.enums.BooleanEnum;
import com.gameplat.common.enums.LimitEnums;
import com.gameplat.common.model.bean.limit.AdminLoginLimit;
import com.gameplat.security.SecurityUserHolder;
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
public class RWLimitInterceptor implements HandlerInterceptor {


  @Autowired private LimitInfoService limitService;

  @Autowired private TwoFactorAuthenticationService twoFactorAuthenticationService;


  @Override
  public boolean preHandle(
      @NotNull HttpServletRequest request,
      @NotNull HttpServletResponse response,
      @NotNull Object handler) {

    AdminLoginLimit adminLoginLimit = limitService.get(LimitEnums.ADMIN_LOGIN_CONFIG);

    // 验证验证码
    this.checkCaptchaCode(adminLoginLimit.getGoogleAuthSwitch(), request);
    return true;
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
