package com.gameplat.admin.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 游戏VO
 * @author kenvin
 */
@Data
@ApiModel("游戏VO")
public class GameVO implements Serializable {

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "游戏编码")
    private String gameCode;

    @ApiModelProperty(value = "游戏中文名")
    private String chineseName;

    @ApiModelProperty(value = "游戏英文名")
    private String englishName;

    @ApiModelProperty(value = "图片名称")
    private String imageName;

    @ApiModelProperty(value = "游戏平台")
    private String platformCode;

    @ApiModelProperty(value = "游戏类型")
    private String gameType;

    @ApiModelProperty(value = "游戏类别")
    private String gameKind;

    @ApiModelProperty(value = "是否支持H5")
    private Integer isH5;

    @ApiModelProperty(value = "是否支持电脑端")
    private Integer isFlash;

    @ApiModelProperty(value = "H5图片")
    private String h5ImageName;

    @ApiModelProperty(value = "游戏排序")
    private Integer sort;
}
