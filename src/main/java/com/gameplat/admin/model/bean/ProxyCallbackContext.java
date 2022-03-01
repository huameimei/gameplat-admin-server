package com.gameplat.admin.model.bean;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/** 第三方代付回调参数封装类 */
@Data
public class ProxyCallbackContext {

  private String ip;

  private String url;

  private String method;

  private List<NameValuePair> headers;

  private String name;

  private String version;

  private String charset;

  private String cashOrderNo;

  private BigDecimal proxyAmount;

  private Map<String, String> merchantParameters;

  private Map<String, String> callbackParameters;

  private String requestBody;

  @Override
  public String toString() {
    return JSON.toJSONString(this);
  }
}
