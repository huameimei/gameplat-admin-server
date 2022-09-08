package com.gameplat.admin.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lily
 * @description 新增福利入参
 * @date 2021/11/22
 */
@Data
public class MemberWealAddDTO implements Serializable {

  @Schema(description = "福利名称")
  private String name;

  @Schema(description = "类型 0：周俸禄  1：月俸禄  2：生日礼金 3：每月红包")
  private Integer type;

  @Schema(description = "0：福利周期  1：VIP等级配置")
  private Integer model;

  @Schema(description = "周期  开始时间")
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
  private Date startDate;

  @Schema(description = "周期  结束时间")
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
  private Date endDate;

  @Schema(description = "最低充值金额")
  private BigDecimal minRechargeAmount;

  @Schema(description = "最低有效投注金额")
  private BigDecimal minBetAmount;

  @Schema(description = "福利备注")
  private String remark;

  @Schema(description = "福利描述")
  private String depict;

  @Schema(description = "0:未结算  1：未派发   2：已派发  3：已回收")
  private Integer status;

  @Schema(description = "福利奖励打码量倍数")
  private BigDecimal wealRewordDama;
}
