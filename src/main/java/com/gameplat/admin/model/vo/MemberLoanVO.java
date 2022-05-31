package com.gameplat.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lily
 * @description 借呗
 * @date 2022/3/6
 */
@Data
public class MemberLoanVO implements Serializable {

  @Schema(description = "会员Id")
  private Long memberId;

  @Schema(description = "会员账号")
  private String account;

  @Schema(description = "VIP等级")
  private Integer vipLevel;

  @Schema(description = "账户余额")
  private BigDecimal memberBalance;

  @Schema(description = "借呗额度")
  private BigDecimal loanMoney;

  @Schema(description = "借款状态  0:未结清  1:已结清  2:已回收")
  private Integer loanStatus;

  @Schema(description = "欠款金额")
  private BigDecimal overdraftMoney;

  @Schema(description = "借款时间")
  private Date loanTime;

  @Schema(description = "操作类型")
  private Integer type;
}
