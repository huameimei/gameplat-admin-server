package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/** 活动大厅更新状态DTO @Author: kenvin @Description: */
@Data
@EqualsAndHashCode(callSuper = false)
public class ActivityLobbyUpdateStatusDTO implements Serializable {

  private static final long serialVersionUID = 6060013282905693277L;

  @Schema(description = "主键")
  private Long id;

  @NotNull(message = "活动类型必选")
  @Schema(description = "活动类型（1 充值活动，2 游戏活动")
  private Integer activityType;

  @NotBlank(message = "活动状态必选")
  @Schema(description = "描述")
  private String description;

  @Schema(description = "ip黑名单")
  private String ipBlacklist;

  @NotNull(message = "活动状态必选")
  @Schema(description = "活动状态（0 关闭，1 开启，2 失效）")
  private Integer status;

  @Schema(description = "是否可以重复参加")
  private Integer isRepeat;

  @Schema(description = "是否自动派发")
  private Integer isAutoDistribute;
}
