package com.gameplat.admin.model.vo.activity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author dl
 * @since 2020-07-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SysLiveLevel对象", description="")
public class SysLiveLevelVO implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "直播间等级")
    private Integer liveLevel;

    @ApiModelProperty(value = "直播间等级名称")
    private String liveLevelName;

    @ApiModelProperty(value = "图标")
    private String iconUrl;

    @ApiModelProperty(value = "背景")
    private String backgroundUrl;

    @ApiModelProperty(value = "总经验值")
    private Long sumExperience;

    @ApiModelProperty(value = "所需经验值")
    private Long needExperience;

    @ApiModelProperty(value = "每日上限")
    private Long everydayLimit;

    @ApiModelProperty(value = "经验值加成")
    private Integer experienceAddition;

}
