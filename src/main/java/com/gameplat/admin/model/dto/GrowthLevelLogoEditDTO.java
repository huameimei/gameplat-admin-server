package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author lily
 * @description
 * @date 2022/1/16
 */
@Data
public class GrowthLevelLogoEditDTO implements Serializable {

  @Schema(description = "主键")
  @NotNull(message = "id不能为空")
  private Long id;

  @Schema(description = "移动端VIP图片")
  private String mobileVipImage;

  @Schema(description = "WEB端VIP图片")
  private String webVipImage;

  @Schema(description = "移动端达成背景图片")
  private String mobileReachBackImage;

  @Schema(description = "移动端未达成背景图片")
  private String mobileUnreachBackImage;

  @Schema(description = "移动端达成VIP图片")
  private String mobileReachVipImage;

  @Schema(description = "移动端未达成VIP图片")
  private String mobileUnreachVipImage;

  @Schema(description = "WEB端达成VIP图片")
  private String webReachVipImage;

  @Schema(description = "WEB端未达成VIP图片")
  private String webUnreachVipImage;
}
