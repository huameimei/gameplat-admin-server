package com.gameplat.admin.feign;


import com.gameplat.admin.model.bean.ProxyDispatchContext;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "payment-service")
public interface PaymentCenterFeign {

   @PostMapping("/api/internal/proxy-pay-server/proxyPay/onlineProxyPay/{code}/{name}")
   void onlineProxyPay(@RequestBody ProxyDispatchContext context,@PathVariable String code,@PathVariable String name);

}
