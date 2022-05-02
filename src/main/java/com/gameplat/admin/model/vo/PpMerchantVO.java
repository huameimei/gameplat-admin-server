package com.gameplat.admin.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameplat.admin.util.Date2LongSerializerUtils;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class PpMerchantVO implements Serializable {

  private Long id;

  private String name;

  private String ppInterfaceCode;

  private String parameters;

  private Integer status;

  private Long proxyTimes;

  private BigDecimal proxyAmount;

  private String interfaceName;

  private Integer sort;

  private String merLimits;

  private BigDecimal maxLimitCash; // 最大金额限制

  private BigDecimal minLimitCash; // 最小金额限制

  private String userLever; // 用户层级

  private PpInterfaceVO ppInterfaceVO;

  private String createBy;

  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date createTime;

  private String updateBy;

  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date updateTime;
}
