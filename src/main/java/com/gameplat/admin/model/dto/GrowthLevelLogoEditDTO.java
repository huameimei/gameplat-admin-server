package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty("主键")
    @NotNull(message = "id不能为空")
    private Long id;

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
}
