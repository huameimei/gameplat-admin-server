package com.gameplat.admin.interceptor;

import com.gameplat.admin.cache.AdminCache;
import com.gameplat.admin.service.CommonService;
import com.gameplat.admin.service.SysUserService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.common.compent.captcha.CaptchaStrategyContext;
import com.gameplat.common.enums.BooleanEnum;
import com.gameplat.common.lang.Assert;
import com.gameplat.common.model.bean.limit.AdminLoginLimit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录拦截器
 *
 * @author robben
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {

  private static final String ACCOUNT = "account";

  @Autowired private CommonService commonService;

  @Autowired private CaptchaStrategyContext captchaStrategyContext;

  @Autowired private AdminCache adminCache;

  @Autowired private SysUserService userService;

  @Override
  public boolean preHandle(
      HttpServletRequest request, HttpServletResponse response, Object handler) {
    AdminLoginLimit limit = Assert.notNull(commonService.getLoginLimit(), "登录配置信息未配置");

    // 验证验证码
    this.checkCaptchaCode(limit.getLoginCaptchaSwitch(), request);

    // 判断密码错误次数
    String account = request.getParameter(ACCOUNT);
    this.checkPasswordErrorCount(limit.getPwdErrorCount(), account);

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
      captchaStrategyContext.getImage().verify(request);
    }
  }

  /**
   * 判断密码错误次数
   *
   * @param pwdErrorCount 密码错误次数
   * @param account 账号
   */
  private void checkPasswordErrorCount(int pwdErrorCount, String account) {
    int errorCount = adminCache.getErrorPasswordCount(account);
    if (errorCount >= pwdErrorCount) {
      userService.disableAccount(account);
      throw new ServiceException("密码错误次数过多，如需解除限制，请联系管理员!");
    }
  }
}
