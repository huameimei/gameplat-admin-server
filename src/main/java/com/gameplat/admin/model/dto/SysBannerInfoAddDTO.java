package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * banner信息DTO
 *
 * @author admin
 */
@Data
public class SysBannerInfoAddDTO implements Serializable {

    /**
     * banner类型
     */
    @ApiModelProperty("banner类型")
    private Integer bannerType;

    /**
     * banner子类型
     */
    @ApiModelProperty("banner子类型")
    private Integer childType;

    /**
     * banner子类型名称
     */
    @ApiModelProperty("banner子类型名称")
    private String childName;

    /**
     * pc端图片地址
     */
    @ApiModelProperty("pc端图片地址")
    private String pcPicUrl;

    /**
     * app端图片地址
     */
    @ApiModelProperty("app端图片地址")
    private String appPicUrl;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer status;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 语种
     */
    @ApiModelProperty("语种")
    private String language;

    @ApiModelProperty("跳转地址")
    private String jumpUrl;

    @ApiModelProperty("游戏类别")
    private String gameKind;

    @ApiModelProperty("关联游戏")
    private String gameCode;

}
