package com.gameplat.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/** @Description : 下级会员佣金 @Author : cc @Date : 2022/3/23 */
@Data
public class MemberCommissionVO implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "累计上月")
  private BigDecimal negativeProfit;

  @Schema(description = "公司净盈利")
  private BigDecimal netProfit;

  @Schema(description = "佣金比例")
  private BigDecimal memberProportion;

  @Schema(description = "佣金")
  private BigDecimal memberCommission;
}
