package com.gameplat.admin.model.vo;

import io.swagger.annotations.ApiModelProperty;
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

  @ApiModelProperty("欠款金额总计")
  private BigDecimal total;
}
