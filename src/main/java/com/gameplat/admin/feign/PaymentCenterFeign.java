package com.gameplat.admin.feign;

import com.gameplat.basepay.proxypay.thirdparty.ProxyCallbackContext;
import com.gameplat.basepay.proxypay.thirdparty.ProxyDispatchContext;
import com.gameplat.common.game.config.FeignRestConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "payment-service", configuration = FeignRestConfig.class)
public interface PaymentCenterFeign {

  @PostMapping("/api/internal/proxyPay/onlineProxyPay/{code}/{name}")
  String onlineProxyPay(
      @RequestBody ProxyDispatchContext context,
      @PathVariable("code") String code,
      @PathVariable("name") String name);

  @PostMapping("/api/internal/proxyPay/onlineQueryProxyPay/{code}/{name}")
  String onlineQueryProxyPay(
      @RequestBody ProxyDispatchContext context,
      @PathVariable("code") String code,
      @PathVariable("name") String name);

  @PostMapping("/api/internal/proxyPay/asyncCallbackProxyPay/{code}/{name}")
  String asyncCallbackProxyPay(
      @RequestBody ProxyCallbackContext context,
      @PathVariable("code") String code,
      @PathVariable("name") String name);
}
