package com.gameplat.admin.interceptor;

import com.gameplat.admin.cache.AdminCache;
import com.gameplat.admin.service.CommonService;
import com.gameplat.admin.service.SysAuthIpService;
import com.gameplat.base.common.util.IPUtils;
import com.gameplat.common.compent.captcha.CaptchaStrategyContext;
import com.gameplat.common.enums.BooleanEnum;
import com.gameplat.common.lang.Assert;
import com.gameplat.common.model.bean.limit.AdminLoginLimit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录拦截器
 *
 * @author robben
 */
public class LoginInterceptor implements HandlerInterceptor {

  private static final String ACCOUNT = "account";

  @Autowired private CommonService commonService;

  @Autowired private SysAuthIpService authIpService;

  @Autowired private CaptchaStrategyContext captchaStrategyContext;

  @Autowired private AdminCache adminCache;

  @Override
  public boolean preHandle(
      HttpServletRequest request, HttpServletResponse response, Object handler) {
    AdminLoginLimit limit = Assert.notNull(commonService.getLoginLimit(), "登录配置信息未配置");

    // 检查IP是否在白名单内
    this.checkIpWhiteList(limit.getIpWhiteListSwitch(), IPUtils.getIpAddress(request));

    // 验证验证码
    this.checkCaptchaCode(limit.getLoginCaptchaSwitch(), request);

    // 判断密码错误次数
    this.checkPasswordErrorCount(limit.getPwdErrorCount(), request.getParameter(ACCOUNT));

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
   * 验证IP白名单
   *
   * @param ipWhiteListSwitch 是否开启了白名单
   * @param ip IP
   */
  private void checkIpWhiteList(int ipWhiteListSwitch, String ip) {
    if (BooleanEnum.YES.match(ipWhiteListSwitch)) {
      Assert.isTrue(authIpService.isPermitted(ip), "当前IP不允许登录：{}", ip);
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
    Assert.isTrue(errorCount < pwdErrorCount, "密码错误次数过多，如需解除限制，请联系管理员!");
  }
}
