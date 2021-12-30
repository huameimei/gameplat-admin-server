package com.gameplat.admin.model.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameplat.admin.util.Date2LongSerializerUtils;
import lombok.Data;

@Data
public class TpMerchantVO extends Model<TpMerchantVO> {

  private Long id;

  private String name;

  private String tpInterfaceCode;

  private Integer status;

  private Long rechargeTimes;

  private BigDecimal rechargeAmount;

  private String interfaceName;

  private String payTypes;

  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date createTime;

  private String createBy;

  private String updateBy;

  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date updateTime;


}
