package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 沙漠
 * @since 2020-06-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ActivityRedPacketConditionAddDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "主键")
  private Long conditionId;

  @Schema(description = "红包id")
  private Long redPacketId;

  @Schema(description = "充值金额")
  private Integer topUpMoney;

  @Schema(description = "抽奖次数")
  private Integer drawNum;

  @Schema(description = "红包雨最小金额")
  private Integer packetMinMoney;

  @Schema(description = "红包雨最大金额")
  private Integer packetMaxMoney;

  @Schema(description = "备注")
  private String remark;

  @Schema(description = "创建人")
  private String createBy;

  @Schema(description = "创建时间")
  private Date createTime;

  @Schema(description = "更新人")
  private String updateBy;

  @Schema(description = "更新时间")
  private Date updateTime;
}
