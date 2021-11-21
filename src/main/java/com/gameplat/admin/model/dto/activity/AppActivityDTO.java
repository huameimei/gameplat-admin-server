package com.gameplat.admin.model.dto.activity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author bhf
 * @Description 活动公共交互数据
 * @Date 2020/6/10 11:57
 **/
@Data
public class AppActivityDTO {

    @ApiModelProperty(value = "活动编号")
    private String activityId;

    @ApiModelProperty(value = "活动类型 （1：红包雨 2：转盘）")
    private Integer activityType;

    @ApiModelProperty(value = "平台类型")
    private String platformType;

    @ApiModelProperty(value = "展示位置")
    private String display;
}
