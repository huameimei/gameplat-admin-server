package com.gameplat.admin.config;

import com.gameplat.admin.interceptor.*;
import com.gameplat.admin.service.MemberGrowthConfigService;
import com.gameplat.security.authz.URIAdapter;
import com.gameplat.web.config.web.WebMvcConfigurationAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig extends WebMvcConfigurationAdapter {

  private final URIAdapter uriAdapter;

  private final MemberGrowthConfigService memberGrowthConfigService;

  @Bean
  public LocaleChangeInterceptor localeChangeInterceptor() {
    log.info("----初始化国际化拦截器----");
    LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
    lci.setParamName("lang");
    return lci;
  }

  @Bean
  public LoginInterceptor loginInterceptor() {
    log.info("----初始化登录拦截器----");
    return new LoginInterceptor();
  }

  @Bean
  public IpWhitelistInterceptor ipWhitelistInterceptor() {
    log.info("----初始化IP白名单拦截器----");
    return new IpWhitelistInterceptor();
  }

  @Bean
  public TwoFactorAuthenticationInterceptor twoFactorAuthenicationInterceptor() {
    log.info("----初始化两步验证拦截器----");
    return new TwoFactorAuthenticationInterceptor();
  }

  @Bean
  public VipInterceptor vipInterceptor() {
    log.info("----初始化VIP验证拦截器----");
    return new VipInterceptor(memberGrowthConfigService);
  }

  @Bean
  public SecurityValidationInterceptor securityValidationInterceptor() {
    log.info("----初始化安全校验拦截器----");
    return new SecurityValidationInterceptor();
  }

  @Bean
  public ForceChangePasswordInterceptor forceChangePasswordInterceptor() {
    log.info("----初始化强制修改密码拦截器----");
    return new ForceChangePasswordInterceptor();
  }

  @Bean
  public RWLimitInterceptor rwLimitInterceptor() {
    log.info("----初始化充提验证谷歌码拦截器----");
    return new RWLimitInterceptor();
  }

  @Override
  protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
    argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    super.addArgumentResolvers(argumentResolvers);
  }

  @Override
  public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
    configurer
        .favorParameter(false)
        .ignoreAcceptHeader(false)
        .defaultContentType(MediaType.APPLICATION_JSON);
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry
        .addInterceptor(localeChangeInterceptor())
        .addPathPatterns("/**")
        .excludePathPatterns(
            "/webjars/*", "/**.html", "/swagger-resources/**", "/actuator/refresh");

    registry.addInterceptor(ipWhitelistInterceptor()).addPathPatterns("/api/admin/**");
    registry.addInterceptor(loginInterceptor()).addPathPatterns("/api/admin/auth/login");
    registry
        .addInterceptor(securityValidationInterceptor())
        .excludePathPatterns(uriAdapter.getPermitUri())
        .addPathPatterns("/api/admin/**");

    registry
        .addInterceptor(twoFactorAuthenicationInterceptor())
        .addPathPatterns("/api/admin/**")
        .excludePathPatterns(uriAdapter.getPermitUri())
        .excludePathPatterns(
            "/api/admin/profile/info",
            "/api/admin/profile/menuList",
            "/api/admin/auth/authCode",
            "/api/admin/auth/bindSecret");

    registry
        .addInterceptor(vipInterceptor())
        .addPathPatterns("api/admin/member/weal/**")
        .excludePathPatterns("api/admin/member/weal/list");

    registry
        .addInterceptor(rwLimitInterceptor())
        .addPathPatterns(
            "/api/admin/finance/rechargeOrder/accept",
            "/api/admin/finance/proxyPay/relProxyPay",
            "/api/admin/finance/memberWithdraw/modifyCashStatus",
            "api/admin/finance/rechargeOrder/batchAccept",
            "api/admin/finance/memberWithdraw/batchWithdraw");
  }
}
