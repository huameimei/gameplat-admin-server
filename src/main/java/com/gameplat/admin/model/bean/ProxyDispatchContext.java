package com.gameplat.admin.model.bean;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/** 第三方代付参数封装类 */
@Data
public class ProxyDispatchContext {

  private String name;

  private String version;

  private String charset;

  private Integer dispatchType;

  private String dispatchUrl;

  private String dispatchMethod;

  private String asyncCallbackUrl;

  private String syncCallbackUrl;

  /** 银行编码 */
  private String bankCode;

  /** 代付订单号 */
  private String proxyOrderNo;

  /** 代付金额 */
  private BigDecimal proxyAmount;

  /** 订单时间 */
  private Date orderTime;

  /** 收款人账号 */
  private String userAccount;

  /** 收款人姓名 */
  private String userRealName;

  /** 开户行所在地址 */
  private String bankCity;

  /** 银行账号 */
  private String bankAccountNo;

  /** 用户IP */
  private String userIpAddress;

  /** 商户号，商户秘钥等参数 */
  private Map<String, String> merchantParameters;

  /** 银行名称 */
  private String bankName;

  /** 项目绝对路径 */
  private String sysPath;

  @Override
  public String toString() {
    return JSON.toJSONString(this);
  }
}
