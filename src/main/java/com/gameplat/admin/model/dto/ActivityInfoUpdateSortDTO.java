package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 更新活动排序DTO
 *
 * @author kenvin
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ActivityInfoUpdateSortDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  @NotNull(message = "ID不能为空")
  @Min(value = 1, message = "ID必须大于0")
  @Schema(description = "编号ID")
  private Long id;

  @NotNull(message = "排序不能为空")
  @Min(value = 1, message = "排序必须大于0")
  @Schema(description = "排序")
  private Integer sort;
}
