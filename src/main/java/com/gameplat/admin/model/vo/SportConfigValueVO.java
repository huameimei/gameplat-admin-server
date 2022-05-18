package com.gameplat.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 体育设置对象 sport_setting_value
 *
 * @author james
 * @date 2022-02-06
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SportConfigValueVO {

  private static final long serialVersionUID = 1L;

  @Schema(description = "app活动背景图")
  private String appActivityImg;

  @Schema(description = "h5活动背景图")
  private String h5ActivityImg;

  @Schema(description = "客服app下载地址")
  private String customerDownloadUrl;

  @Schema(description = "客服地址")
  private String customerUrl;

  @Schema(description = "场景标识")
  private Integer scene;

  @Schema(description = "默认盘口 1:欧盘 2:港盘")
  private Integer handicap;

  @Schema(description = "彩票开关")
  private String cpChatEnable;

  private String viewState;
  private String eventValidation;

  @Schema(description = "版型选择")
  private String style;

  @Schema(description = "球头显示规则")
  private String ballHeadRule;

  @Schema(description = "体育球类导航")
  private String sportBallNavigation;

  @Schema(description = "体育联赛导航")
  private String sportLeagueNavigation;

  @Schema(description = "开关与排序表")
  private List<ListSortConfigVO> listSortConfigs;
}
