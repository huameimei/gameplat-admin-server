package com.gameplat.admin.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameplat.admin.util.Date2LongSerializerUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lily
 * @description
 * @date 2022/2/5
 */
@Data
public class LogoConfigVO implements Serializable {

  @Schema(description = "主键")
  private Long id;

  @Schema(description = "等级")
  private Integer level;

  @Schema(description = "等级名称")
  private String levelName;

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

  @Schema(description = "更新人")
  private String updateBy;

  @Schema(description = "更新时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date updateTime;
}
