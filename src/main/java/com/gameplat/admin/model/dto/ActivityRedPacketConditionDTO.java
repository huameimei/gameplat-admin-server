package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author lyq @Description
 * @date 2020年6月16日 下午2:36:20
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ActivityRedPacketConditionDTO implements Serializable {
  /** */
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
}
