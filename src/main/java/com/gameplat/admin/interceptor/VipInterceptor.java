package com.gameplat.admin.interceptor;

import com.gameplat.admin.enums.LanguageEnum;
import com.gameplat.admin.model.vo.MemberGrowthConfigVO;
import com.gameplat.admin.service.MemberGrowthConfigService;
import com.gameplat.base.common.enums.EnableEnum;
import com.gameplat.common.lang.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * VIP拦截器
 *
 * @author robben
 */
@RequiredArgsConstructor
public class VipInterceptor extends HandlerInterceptorAdapter {

  private final MemberGrowthConfigService memberGrowthConfigService;

  @Override
  public boolean preHandle(
      HttpServletRequest request, HttpServletResponse response, Object handler) {
    MemberGrowthConfigVO isVip =
        memberGrowthConfigService.findOneConfig(LanguageEnum.app_zh_CN.getCode());
    Assert.state(EnableEnum.isEnabled(isVip.getIsEnableVip()), "未开启VIP功能");

    return true;
  }
}
