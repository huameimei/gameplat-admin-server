package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 更新banner状态
 *
 * @author kenvin
 */
@Data
public class SysBannerInfoUpdateStatusDTO implements Serializable {

  @NotNull(message = "ID不能为空")
  @Min(value = 1, message = "ID必须大于0")
  @Schema(description = "主键ID")
  private Long id;

  @NotNull(message = "状态不能为空")
  @Schema(description = "状态（0禁用 1启用）")
  private Integer status;
}
