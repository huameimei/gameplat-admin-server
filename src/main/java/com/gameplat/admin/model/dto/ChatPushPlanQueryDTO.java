package com.gameplat.admin.model.dto;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/** 聊天室推送计划 */
@Data
@TableName("chat_push_plan")
public class ChatPushPlanQueryDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty(value = "账号")
  private String account;

  @ApiModelProperty(value = "聊天室id")
  private Long roomId;

  @ApiModelProperty(value = "房间名")
  private String roomName;

  @ApiModelProperty(value = "游戏id")
  private String gameId;

}
