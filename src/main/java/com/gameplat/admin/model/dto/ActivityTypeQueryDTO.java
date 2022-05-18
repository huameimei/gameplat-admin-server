package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 活动类型查询DTO
 *
 * @author kenvin
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ActivityTypeQueryDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "活动类型")
  private String typeCode;

  @Schema(description = "活动类型名称")
  private String typeName;

  @Schema(description = "状态,0 无效,1 有效")
  private Integer typeStatus;

  @Schema(description = "浮窗状态,0 无效,1 有效")
  private Integer floatStatus;

  @Schema(description = "语言")
  private String language;
}
