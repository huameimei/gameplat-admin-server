package com.gameplat.admin.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/** @Description : 下级会员佣金 @Author : cc @Date : 2022/3/23 */
@Data
public class MemberCommissionVO implements Serializable {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty(value = "累计上月")
  private BigDecimal negativeProfit;

  @ApiModelProperty(value = "公司净盈利")
  private BigDecimal netProfit;

  @ApiModelProperty(value = "佣金比例")
  private BigDecimal memberProportion;

  @ApiModelProperty(value = "佣金")
  private BigDecimal memberCommission;
}
