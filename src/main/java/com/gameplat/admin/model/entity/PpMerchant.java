package com.gameplat.admin.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gameplat.common.model.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pp_merchant")
public class PpMerchant extends BaseEntity<PpMerchant> {

  @ApiModelProperty(value = "名称")
  private String name;

  @ApiModelProperty(value = "第三方代付接口编码")
  private String ppInterfaceCode;

  @ApiModelProperty(value = "状态: [0 - 启用, 1 - 禁用]")
  private Integer status;

  @ApiModelProperty(value = "代付累计次数")
  private Long proxyTimes;

  @ApiModelProperty(value = "代付累计金额")
  private Long proxyAmount;

  @ApiModelProperty(value = "商户参数JSON")
  private String parameters;

  @ApiModelProperty(value = "商户限制信息JSON")
  private String merLimits;

  @ApiModelProperty(value = "排序")
  private Integer sort;
}
