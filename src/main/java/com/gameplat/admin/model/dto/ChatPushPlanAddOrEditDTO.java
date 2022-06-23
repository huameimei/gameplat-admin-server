package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/** 聊天室推送计划 */
@Data
public class ChatPushPlanAddOrEditDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "主键ID")
  private Long id;

  @Schema(description = "用户ID")
  private Long userId;

  @Schema(description = "账号")
  @NotNull(message = "会员账号不能为空！")
  private String account;

  @Schema(description = "聊天室id")
  @NotNull(message = "聊天室id不能为空！")
  private Long roomId;

  @Schema(description = "游戏id")
  @NotNull(message = "彩种id不能为空！")
  private String gameId;

  @Schema(description = "游戏名称")
  private String gameName;

  @Schema(description = "中奖最小金额")
  @NotNull(message = "中奖最小金额不能为空！")
  private double minWinMoney;

  @Schema(description = "中奖最大金额")
  @NotNull(message = "中奖最大金额不能为空！")
  private double maxWinMoney;

  @Schema(description = "开始时间")
  @NotNull(message = "推送开始时间不能为空！")
  private Long beginTime;

  @Schema(description = "结束时间")
  @NotNull(message = "推送结束时间不能为空！")
  private Long endTime;

  @Schema(description = "推送间隔")
  @NotNull(message = "推送间隔不能为空！")
  private Integer interval;

  @Schema(description = "状态1启用，0禁用")
  private Integer state;

  @Schema(description = "最后推送时间")
  private Long lastPushTime;

  @Schema(description = "房间名")
  private String roomName;

  @Schema(description = "彩种状态")
  private int gameStatus;

  @Schema(description = "创建时间")
  private Long createTime;

  @Schema(description = "修改时间")
  private Long updateTime;
}
