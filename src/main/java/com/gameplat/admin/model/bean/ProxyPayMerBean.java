package com.gameplat.admin.model.bean;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.math.BigDecimal;

/** 转换类 */
@Data
public class ProxyPayMerBean {

  private BigDecimal maxLimitCash; // 最大金额限制

  private BigDecimal minLimitCash; // 最小金额限制

  private String userLever; // 用户层级

  public ProxyPayMerBean() {}

  public ProxyPayMerBean(BigDecimal maxLimitCash, BigDecimal minLimitCash, String userLever) {
    this.maxLimitCash = maxLimitCash;
    this.minLimitCash = minLimitCash;
    this.userLever = userLever;
  }

  public static ProxyPayMerBean conver2Bean(String beanStr) {
    return JSON.parseObject(beanStr, ProxyPayMerBean.class);
  }

  public static String conver2LimitStr(ProxyPayMerBean proxyPayMerBean) {
    return JSON.toJSONString(proxyPayMerBean);
  }

  @Override
  public String toString() {
    return JSON.toJSONString(this);
  }
}
