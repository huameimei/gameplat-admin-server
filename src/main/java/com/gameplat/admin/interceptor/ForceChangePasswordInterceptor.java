package com.gameplat.admin.interceptor;

import com.gameplat.admin.service.SysUserService;
import com.gameplat.base.common.enums.EnableEnum;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.model.entity.sys.SysUser;
import com.gameplat.security.SecurityUserHolder;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 强制修改密码拦截器
 *
 * @author robben
 */
public class ForceChangePasswordInterceptor implements HandlerInterceptor {

  @Autowired private SysUserService userService;

  @Override
  public boolean preHandle(
          @NotNull HttpServletRequest request,
          @NotNull HttpServletResponse response,
          @NotNull Object handler) {
    SysUser user = userService.getById(SecurityUserHolder.getUserId());
    if (EnableEnum.isEnabled(user.getChangeFlag())) {
      throw new ServiceException(10011, "为了您的账号信息安全，请修改密码");
    }

    return true;
  }
}
