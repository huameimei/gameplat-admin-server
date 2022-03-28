package com.gameplat.admin.service;

import com.gameplat.admin.model.bean.ReturnMessage;
import com.gameplat.basepay.pay.bean.NameValuePair;
import com.gameplat.security.context.UserCredential;

import java.util.List;
import java.util.Map;

public interface ProxyPayService {

  void proxyPay(
      Long id,
      Long ppMerchantId,
      String asyncCallbackUrl,
      String sysPath,
      UserCredential userCredential)
      throws Exception;

  ReturnMessage queryProxyOrder(Long id, Long ppMerchantId) throws Exception;

  String proxyPayAsyncCallback(
      String orderNo,
      String url,
      String method,
      List<NameValuePair> headers,
      String ipAddress,
      Map<String, String> callbackParameters,
      String requestBody)
      throws Exception;
}
