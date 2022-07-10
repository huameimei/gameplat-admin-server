package com.gameplat.admin.interceptor;

import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.IPUtils;
import com.gameplat.security.SecurityUserHolder;
import com.gameplat.security.context.UserCredential;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 安全校验
 *
 * @author robben
 */
public class SecurityValidationInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(
      @NotNull HttpServletRequest request,
      @NotNull HttpServletResponse response,
      @NotNull Object handler) {
    UserCredential credential = SecurityUserHolder.getCredential();
    String requestIp = IPUtils.getIpAddress(request);
    if (!credential.getLoginIp().equals(requestIp)) {
      throw new ServiceException("IP发生变更，您已被迫下线!");
    }
    return true;
  }
}
