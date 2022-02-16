package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/** 聊天室推送计划 */
@Data
@TableName("chat_push_plan")
public class ChatPushPlan implements Serializable {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty(value = "主键ID")
  @TableId(type = IdType.AUTO)
  private Long id;

  @ApiModelProperty(value = "用户ID")
  private Long userId;

  @ApiModelProperty(value = "账号")
  private String account;

  @ApiModelProperty(value = "聊天室id")
  private Long roomId;

  @ApiModelProperty(value = "游戏id")
  private String gameId;

  @ApiModelProperty(value = "游戏名称")
  private String gameName;

  @ApiModelProperty(value = "中奖最小金额")
  private double minWinMoney;

  @ApiModelProperty(value = "中奖最大金额")
  private double maxWinMoney;

  @ApiModelProperty(value = "开始时间")
  private Long beginTime;

  @ApiModelProperty(value = "结束时间")
  private Long endTime;

  @ApiModelProperty(value = "推送间隔")
  private Integer interval;

  @ApiModelProperty(value = "状态1启用，0禁用")
  private Integer state;

  @ApiModelProperty(value = "最后推送时间")
  private Long lastPushTime;

  @ApiModelProperty(value = "房间名")
  private String roomName;

  @ApiModelProperty(value = "彩种状态")
  private int gameStatus;

  @ApiModelProperty(value = "创建人")
  @TableField(fill = FieldFill.INSERT)
  private String createBy;

  @ApiModelProperty(value = "创建时间")
  @TableField(fill = FieldFill.INSERT)
  private Long createTime;

  @ApiModelProperty(value = "修改人")
  @TableField(fill = FieldFill.INSERT_UPDATE)
  private String updateBy;

  @ApiModelProperty(value = "修改时间")
  @TableField(fill = FieldFill.INSERT_UPDATE)
  private Long updateTime;
}
