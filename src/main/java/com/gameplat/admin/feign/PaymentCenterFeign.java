package com.gameplat.admin.feign;

import com.gameplat.admin.model.bean.ReturnMessage;
import com.gameplat.base.common.web.Result;
import com.gameplat.basepay.proxypay.thirdparty.ProxyCallbackContext;
import com.gameplat.basepay.proxypay.thirdparty.ProxyDispatchContext;
import com.gameplat.basepay.proxypay.thirdparty.ProxyPayBackResult;
import com.gameplat.common.game.config.FeignRestConfig;
import com.gameplat.web.config.feign.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "payment-service", configuration = FeignRestConfig.class)
public interface PaymentCenterFeign {

  @PostMapping(value = "/api/internal/proxyPay/onlineProxyPay/{code}/{name}", consumes = MediaType.APPLICATION_JSON_VALUE)
  Result<ProxyPayBackResult> onlineProxyPay(
      @RequestBody ProxyDispatchContext context,
      @PathVariable("code") String code,
      @PathVariable("name") String name);

  @PostMapping(value = "/api/internal/proxyPay/onlineQueryProxyPay/{code}/{name}", consumes = MediaType.APPLICATION_JSON_VALUE)
  Result<ReturnMessage> onlineQueryProxyPay(
      @RequestBody ProxyDispatchContext context,
      @PathVariable("code") String code,
      @PathVariable("name") String name);

  @PostMapping(value = "/api/internal/proxyPay/asyncCallbackProxyPay/{code}/{name}", consumes = MediaType.APPLICATION_JSON_VALUE)
  Result<ProxyPayBackResult> asyncCallbackProxyPay(
      @RequestBody ProxyCallbackContext context,
      @PathVariable("code") String code,
      @PathVariable("name") String name);
}
