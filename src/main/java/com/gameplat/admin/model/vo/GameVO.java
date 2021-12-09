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

    @ApiModelProperty(value = "游戏排序")
    private String englishName;

    @ApiModelProperty(value = "游戏排序")
    private String imageName;

    @ApiModelProperty(value = "游戏排序")
    private String platformCode;

    @ApiModelProperty(value = "游戏排序")
    private String gameType;

    @ApiModelProperty(value = "游戏排序")
    private Integer isH5;

    @ApiModelProperty(value = "游戏排序")
    private Integer isFlash;

    @ApiModelProperty(value = "游戏排序")
    private String h5ImageName;

    @ApiModelProperty(value = "游戏排序")
    private Integer sort;
}
