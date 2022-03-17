package com.gameplat.admin.interceptor;

import com.gameplat.base.common.util.IPUtils;
import com.gameplat.security.SecurityUserHolder;
import com.gameplat.security.context.UserCredential;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 安全校验
 *
 * @author robben
 */
public class SecurityValidationInterceptor extends HandlerInterceptorAdapter {

  @Override
  public boolean preHandle(
      HttpServletRequest request, HttpServletResponse response, Object handler) {
    UserCredential credential = SecurityUserHolder.getCredential();
    String requestIp = IPUtils.getIpAddress(request);
    if (!credential.getLoginIp().equals(requestIp)) {
      throw new AccessDeniedException("IP发生变更，您已被迫下线!");
    }
    return true;
  }
}