package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author lily
 * @description 会员权益
 * @date 2022/1/15
 */

@Data
public class MemberWealConfigAddDTO implements Serializable {

    @ApiModelProperty(value = "权益名称")
    @NotNull(message = "权益名称不能为空")
    private String name;

    @ApiModelProperty(value = "最低专享等级")
    @NotNull(message = "最低专享等级不能为空")
    private Integer level;

    @ApiModelProperty(value = "未开启描述")
    @NotNull(message = "未开启描述不能为空")
    private String turnDownDesc;

    @ApiModelProperty(value = "开启描述")
    @NotNull(message = "开启描述不能为空")
    private String turnUpDesc;

    @ApiModelProperty(value = "排序值")
    private Integer sort;

    @ApiModelProperty(value = "是否显示金额 0:不显示 38:晋级礼金金额 36:生日礼金金额 39:每月红包金额")
    private Integer type;

    @ApiModelProperty(value = "H5权益图标")
    private String image;

    @ApiModelProperty(value = "web端权益图标")
    private String webImage;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "语种：zh-CN   en-US   in-ID   th-TH   vi-VN")
    private String language;

}
