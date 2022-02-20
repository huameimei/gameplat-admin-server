package com.gameplat.admin.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameplat.common.util.I18nSerializerUtils;
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

    @ApiModelProperty(value = "游戏名")
    @JsonSerialize(using = I18nSerializerUtils.class)
    private String gameName;

    @ApiModelProperty(value = "PC图片地址")
    private String pcImgUrl;

    @ApiModelProperty(value = "APP图片地址")
    private String appImgUrl;

    @ApiModelProperty(value = "游戏平台")
    private String platformCode;

    @ApiModelProperty(value = "游戏大类")
    private String gameType;

    @ApiModelProperty(value = "游戏类别")
    private String gameKind;

    @ApiModelProperty(value = "是否支持H5 (0：否；1:是)")
    private Integer isH5;

    @ApiModelProperty(value = "是否支持电脑端(0：否；1:是)")
    private Integer isPc;

    @ApiModelProperty(value = "游戏排序")
    private Integer sort;
}
