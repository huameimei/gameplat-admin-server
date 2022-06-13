package com.gameplat.admin.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 活动查询类
 *
 * @author kenvin
 * @since 2020-08-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ActivityInfoQueryDTO implements Serializable {

  @Schema(description = "活动类型")
  private Long type;

  @Schema(description = "申请方式 （1自动 2主动 3在线客服）")
  private Integer applyType;

  @Schema(description = "活动有效状态（1永久有效 2有时限）")
  private Integer validStatus;

  @Schema(description = "活动状态,1 有效,0 无效")
  private Integer status;

  @JsonSerialize(using = ToStringSerializer.class)
  @Schema(description = "关联活动大厅ID")
  private Long activityLobbyId;

  @Schema(description = "创建人")
  private String createBy;
}
