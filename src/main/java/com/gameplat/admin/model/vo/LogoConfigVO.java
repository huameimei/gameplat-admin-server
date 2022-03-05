package com.gameplat.admin.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameplat.admin.util.Date2LongSerializerUtils;
import io.swagger.annotations.ApiModelProperty;
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

  @ApiModelProperty("主键")
  private Long id;

  @ApiModelProperty("等级")
  private Integer level;

  @ApiModelProperty("等级名称")
  private String levelName;

  @ApiModelProperty("移动端VIP图片")
  private String mobileVipImage;

  @ApiModelProperty("WEB端VIP图片")
  private String webVipImage;

  @ApiModelProperty("移动端达成背景图片")
  private String mobileReachBackImage;

  @ApiModelProperty("移动端未达成背景图片")
  private String mobileUnreachBackImage;

  @ApiModelProperty("移动端达成VIP图片")
  private String mobileReachVipImage;

  @ApiModelProperty("移动端未达成VIP图片")
  private String mobileUnreachVipImage;

  @ApiModelProperty("WEB端达成VIP图片")
  private String webReachVipImage;

  @ApiModelProperty("WEB端未达成VIP图片")
  private String webUnreachVipImage;

  @ApiModelProperty(value = "更新人")
  private String updateBy;

  @ApiModelProperty(value = "更新时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date updateTime;
}
