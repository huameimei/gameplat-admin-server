package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/** 活动大厅查询DTO @Author: kenvin @Description: */
@Data
@EqualsAndHashCode(callSuper = false)
public class ActivityLobbyQueryDTO implements Serializable {

  private static final long serialVersionUID = 6060013282905693277L;

  @Schema(description = "标题")
  private String title;

  @Schema(description = "活动状态（0 关闭，1 开启，2 失效）")
  private Integer status;

  @Schema(description = "活动状态（0 关闭，1 开启，2 失效）")
  private Integer failure;

  @Schema(description = "活动类型")
  private Integer type;
}
