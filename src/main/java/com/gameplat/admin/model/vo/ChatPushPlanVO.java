package com.gameplat.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/** 聊天室推送计划 */
@Data
public class ChatPushPlanVO implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "主键ID")
  private Long id;

  @Schema(description = "用户ID")
  private Long userId;

  @Schema(description = "账号")
  private String account;

  @Schema(description = "聊天室id")
  private Long roomId;

  @Schema(description = "游戏id")
  private String gameId;

  @Schema(description = "游戏名称")
  private String gameName;

  @Schema(description = "中奖最小金额")
  private double minWinMoney;

  @Schema(description = "中奖最大金额")
  private double maxWinMoney;

  @Schema(description = "开始时间")
  private Long beginTime;

  @Schema(description = "结束时间")
  private Long endTime;

  @Schema(description = "推送间隔")
  private Integer interval;

  @Schema(description = "状态1启用，0禁用")
  private Integer state;

  @Schema(description = "最后推送时间")
  private Long lastPushTime;

  @Schema(description = "房间名")
  private String roomName;

  @Schema(description = "彩种状态")
  private int gameStatus;
}
