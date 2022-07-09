package com.gameplat.admin.interceptor;

import com.gameplat.admin.service.TwoFactorAuthenticationService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 两步验证拦截器
 *
 * @author robben
 */
public class TwoFactorAuthenticationInterceptor implements HandlerInterceptor {

  @Autowired private TwoFactorAuthenticationService twoFactorAuthenticationService;

  @Override
  public boolean preHandle(
      @NotNull HttpServletRequest request,
      @NotNull HttpServletResponse response,
      @NotNull Object handler)
      throws Exception {
    twoFactorAuthenticationService.isEnabled2Fa();
    return true;
  }
}
