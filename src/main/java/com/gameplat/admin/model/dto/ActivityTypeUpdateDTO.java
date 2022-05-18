package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 活动类型更新DTO
 *
 * @author kenvin
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ActivityTypeUpdateDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  @NotNull(message = "ID不能为空")
  @Min(value = 1, message = "id必须大于0")
  @Schema(description = "编号")
  private Long id;

  @NotBlank(message = "活动类型不能为空")
  @Schema(description = "活动类型")
  private String typeCode;

  @NotBlank(message = "活动类型名称不能为空")
  @Schema(description = "活动类型名称")
  private String typeName;

  @Schema(description = "备注")
  private String remark;

  @NotNull(message = "排序不能为空")
  @Min(value = 0, message = "排序必须大于0")
  @Schema(description = "排序")
  private Integer sort;

  @NotNull(message = "状态不能为空")
  @Schema(description = "状态,0 无效,1 有效")
  private Integer typeStatus;

  @NotNull(message = "浮窗状态不能为空")
  @Schema(description = "浮窗状态,0 无效,1 有效")
  private Integer floatStatus;

  @Schema(description = "浮窗logo")
  private String floatLogo;

  @Schema(description = "浮窗url")
  private String floatUrl;

  @Schema(description = "语言")
  private String language;
}
