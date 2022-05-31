package com.gameplat.admin.feign;

import com.gameplat.basepay.proxypay.thirdparty.ProxyCallbackContext;
import com.gameplat.basepay.proxypay.thirdparty.ProxyDispatchContext;
import com.gameplat.common.constant.ServiceApi;
import com.gameplat.common.game.config.FeignRestConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "paymentCenterFeign",
        url = "${pay.request.host}",
        path = ServiceApi.INNER_API + "/proxyPay",
        configuration = FeignRestConfig.class)
public interface PaymentCenterFeign {

  @PostMapping("/onlineProxyPay/{code}/{name}")
  String onlineProxyPay(
          @RequestBody ProxyDispatchContext context,
          @PathVariable("code") String code,
          @PathVariable("name") String name);

  @PostMapping("/onlineQueryProxyPay/{code}/{name}")
  String onlineQueryProxyPay(
          @RequestBody ProxyDispatchContext context,
          @PathVariable("code") String code,
          @PathVariable("name") String name);

  @PostMapping("/asyncCallbackProxyPay/{code}/{name}")
  String asyncCallbackProxyPay(
          @RequestBody ProxyCallbackContext context,
          @PathVariable("code") String code,
          @PathVariable("name") String name);
}
