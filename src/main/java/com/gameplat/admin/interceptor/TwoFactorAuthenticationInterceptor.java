package com.gameplat.admin.interceptor;

import com.gameplat.admin.service.TwoFactorAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 两步验证拦截器
 *
 * @author robben
 */
public class TwoFactorAuthenticationInterceptor extends HandlerInterceptorAdapter {

  @Autowired private TwoFactorAuthenticationService twoFactorAuthenticationService;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    twoFactorAuthenticationService.isEnabled2Fa();
    return super.preHandle(request, response, handler);
  }
}
