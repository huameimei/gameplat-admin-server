package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lily
 * @description 借呗
 * @date 2022/3/6
 */
@Data
public class MemberLoanQueryDTO implements Serializable {

  @Schema(description = "会员账号")
  private String account;

  @Schema(description = "VIP等级")
  private Integer vipLevel;

  @Schema(description = "欠款金额区间")
  private Double minOverdraftMoney;

  @Schema(description = "欠款金额区间")
  private Double maxOverdraftMoney;

  @Schema(description = "借款时间区间")
  private String beginLoanTime;

  @Schema(description = "借款时间区间")
  private String endLoanTime;

  @Schema(description = "借款状态  0:成功  1:失败  2:已回收")
  private Integer loanStatus;
}
