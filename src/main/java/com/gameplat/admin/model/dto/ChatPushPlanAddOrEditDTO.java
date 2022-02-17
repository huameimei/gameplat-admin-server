package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/** 聊天室推送计划 */
@Data
public class ChatPushPlanAddOrEditDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty(value = "主键ID")
  private Long id;

  @ApiModelProperty(value = "用户ID")
  private Long userId;

  @ApiModelProperty(value = "账号")
  @NotNull(message = "会员账号不能为空！")
  private String account;

  @ApiModelProperty(value = "聊天室id")
  @NotNull(message = "聊天室id不能为空！")
  private Long roomId;

  @ApiModelProperty(value = "游戏id")
  @NotNull(message = "彩种id不能为空！")
  private String gameId;

  @ApiModelProperty(value = "游戏名称")
  private String gameName;

  @ApiModelProperty(value = "中奖最小金额")
  @NotNull(message = "中奖最小金额不能为空！")
  private double minWinMoney;

  @ApiModelProperty(value = "中奖最大金额")
  @NotNull(message = "中奖最大金额不能为空！")
  private double maxWinMoney;

  @ApiModelProperty(value = "开始时间")
  @NotNull(message = "推送开始时间不能为空！")
  private Long beginTime;

  @ApiModelProperty(value = "结束时间")
  @NotNull(message = "推送结束时间不能为空！")
  private Long endTime;

  @ApiModelProperty(value = "推送间隔")
  @NotNull(message = "推送间隔不能为空！")
  private Integer interval;

  @ApiModelProperty(value = "状态1启用，0禁用")
  private Integer state;

  @ApiModelProperty(value = "最后推送时间")
  private Long lastPushTime;

  @ApiModelProperty(value = "房间名")
  private String roomName;

  @ApiModelProperty(value = "彩种状态")
  private int gameStatus;

  @ApiModelProperty(value = "创建时间")
  private Long createTime;

  @ApiModelProperty(value = "修改时间")
  private Long updateTime;


}
