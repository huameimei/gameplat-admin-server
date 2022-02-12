package com.gameplat.admin.config;

import com.gameplat.admin.interceptor.IpWhitelistInterceptor;
import com.gameplat.admin.interceptor.LoginInterceptor;
import com.gameplat.web.config.web.WebMvcConfigurationAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Slf4j
@Configuration
public class WebMvcConfig extends WebMvcConfigurationAdapter {

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

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry
        .addInterceptor(localeChangeInterceptor())
        .addPathPatterns("/**")
        .excludePathPatterns(
            "/webjars/*", "/**.html", "/swagger-resources/**", "/actuator/refresh");
    registry.addInterceptor(ipWhitelistInterceptor()).addPathPatterns("/api/admin/**");
    registry.addInterceptor(loginInterceptor()).addPathPatterns("/api/admin/auth/login");
  }
}
