package com.gameplat.admin.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lily
 * @description 修改福利入参
 * @date 2021/11/22
 */
@Data
public class MemberWealEditDTO implements Serializable {

  @Schema(description = "主键")
  private Long id;

  @Schema(description = "福利名称")
  private String name;

  @Schema(description = "类型 0：周俸禄  1：月俸禄  2：生日礼金 3：每月红包")
  private Integer type;

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

  @Schema(description = "资格会员数")
  private Integer totalUserCount;

  @Schema(description = "总的派发金额")
  private BigDecimal totalPayMoney;

  @Schema(description = "流水号")
  private String serialNumber;

  @Schema(description = "结算时间")
  private Date settleTime;

  @Schema(description = "派发时间")
  private Date payTime;

  @Schema(description = "修改人")
  private String updateBy;

  @Schema(description = "修改时间")
  private Date updateTime;

  @Schema(description = "状态")
  private Integer status;
}
