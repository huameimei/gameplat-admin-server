package com.gameplat.admin.interceptor;

import cn.hutool.core.collection.CollUtil;
import com.gameplat.admin.enums.AuthIpEnum;
import com.gameplat.admin.service.CommonService;
import com.gameplat.admin.service.SysAuthIpService;
import com.gameplat.base.common.util.IPUtils;
import com.gameplat.common.enums.BooleanEnum;
import com.gameplat.common.lang.Assert;
import com.gameplat.common.model.bean.limit.AdminLoginLimit;
import com.gameplat.model.entity.sys.SysAuthIp;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * IP白名单拦截器
 *
 * @author robben
 */
public class IpWhitelistInterceptor implements HandlerInterceptor {

  @Autowired private SysAuthIpService authIpService;

  @Autowired private CommonService commonService;

  @Override
  public boolean preHandle(
      @NotNull HttpServletRequest request,
      @NotNull HttpServletResponse response,
      @NotNull Object handler) {
    AdminLoginLimit limit = Assert.notNull(commonService.getLoginLimit(), "登录配置信息未配置");
    this.checkIpWhiteList(limit.getIpWhiteListSwitch(), IPUtils.getIpAddress(request));
    return true;
  }

  /**
   * 验证IP白名单
   *
   * @param ipWhiteListSwitch 是否开启了白名单
   * @param ip IP
   */
  private void checkIpWhiteList(int ipWhiteListSwitch, String ip) {
    if (BooleanEnum.YES.match(ipWhiteListSwitch)) {
      Assert.isTrue(this.isAllowed(ip), "当前IP：{}访问被拒绝，如有疑问请联系管理员!", ip);
    }
  }

  private boolean isAllowed(String ip) {
    List<SysAuthIp> authIps = authIpService.getAll();
    return CollUtil.isNotEmpty(authIps)
        && authIps.stream()
            .filter(e -> e.getIpType().contains(AuthIpEnum.Type.ADMIN.value()))
            .map(SysAuthIp::getAllowIp)
            .anyMatch(ip::equals);
  }
}
