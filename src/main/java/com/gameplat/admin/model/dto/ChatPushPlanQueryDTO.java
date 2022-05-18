package com.gameplat.admin.model.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/** 聊天室推送计划 */
@Data
@TableName("chat_push_plan")
public class ChatPushPlanQueryDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "账号")
  private String account;

  @Schema(description = "聊天室id")
  private Long roomId;

  @Schema(description = "房间名")
  private String roomName;

  @Schema(description = "游戏id")
  private String gameId;
}
