package com.gameplat.admin.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gameplat.common.model.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pay_type")
public class PayType extends BaseEntity<PayType> {

  private String name;

  private String code;

  private String bankFlag;

  private Integer sort;

  private String rechargeDesc;

  @ApiModelProperty(value = "是否支持转账: [0 - 否, 1 - 是]")
  private Integer transferEnabled;

  @ApiModelProperty(value = "是否支持在线支付: [0 - 否, 1 - 是]")
  private Integer onlinePayEnabled;

  @ApiModelProperty(value = "状态: [0 - 启用, 1 - 禁用]")
  private Integer status;

  @ApiModelProperty(value = "是否系统支付编码: [0 - 否, 1 - 是]")
  private Integer isSysCode;
}
