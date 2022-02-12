package com.gameplat.admin.interceptor;

import com.gameplat.admin.model.domain.SysAuthIp;
import com.gameplat.admin.service.CommonService;
import com.gameplat.admin.service.SysAuthIpService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.IPUtils;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.enums.BooleanEnum;
import com.gameplat.common.lang.Assert;
import com.gameplat.common.model.bean.limit.AdminLoginLimit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Set;

import static com.gameplat.base.common.enums.ResultCode.FORBIDDEN;

/**
 * IP白名单拦截器
 *
 * @author robben
 */
public class IpWhitelistInterceptor extends HandlerInterceptorAdapter {

  @Autowired private SysAuthIpService authIpService;

  @Autowired private CommonService commonService;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    AdminLoginLimit limit = Assert.notNull(commonService.getLoginLimit(), "登录配置信息未配置");
    this.checkIpWhiteList(limit.getIpWhiteListSwitch(), IPUtils.getIpAddress(request));
    return super.preHandle(request, response, handler);
  }

  /**
   * 验证IP白名单
   *
   * @param ipWhiteListSwitch 是否开启了白名单
   * @param ip IP
   */
  private void checkIpWhiteList(int ipWhiteListSwitch, String ip) {
    Set<String> authIps = authIpService.getAllList();
    if (BooleanEnum.YES.match(ipWhiteListSwitch)) {
      String message = StringUtils.format("当前IP：{}访问被拒绝，如有疑问请联系管理员!", ip);
      Assert.isTrue(authIps.contains(ip), () -> new ServiceException(FORBIDDEN, message));
    }
  }
}
