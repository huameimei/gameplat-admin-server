package com.gameplat.admin.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 体育设置对象 sport_setting_value
 * @author james
 * @date 2022-02-06
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SportConfigValueVO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "app活动背景图")
    private String appActivityImg;
    @ApiModelProperty(value = "h5活动背景图")
    private String h5ActivityImg;
    @ApiModelProperty(value = "客服app下载地址")
    private String customerDownloadUrl;
    @ApiModelProperty(value = "客服地址")
    private String customerUrl;
    @ApiModelProperty(value = "场景标识")
    private Integer scene;
    @ApiModelProperty(value = "默认盘口 1:欧盘 2:港盘")
    private Integer handicap;
    @ApiModelProperty(value = "彩票开关")
    private String cpChatEnable;
    private String viewState;
    private String eventValidation;
    @ApiModelProperty(value = "版型选择")
    private String style;
    @ApiModelProperty(value = "球头显示规则")
    private String ballHeadRule;
    @ApiModelProperty(value = "体育球类导航")
    private String sportBallNavigation;
    @ApiModelProperty(value = "体育联赛导航")
    private String sportLeagueNavigation;

    @ApiModelProperty("开关与排序表")
    private List<ListSortConfigVO> listSortConfigs;
}