package com.gameplat.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author lily
 * @description
 * @date 2022/3/9
 */
@Data
public class MemberLoanSumVO implements Serializable {

  @Schema(description = "欠款金额总计")
  private BigDecimal total;
}
