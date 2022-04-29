package com.gameplat.admin.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 体育设置对象 kg_sport_config
 *
 * @author nick
 * @date 2020-08-15
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SportConfigVO {

  private static final long serialVersionUID = 1L;

  /** 主键 */
  private Long id;

  /** bti默认盘口 1 欧盘 2 香港盘 */
  private String btiDefaultPosition;

  /** xj默认盘口 1 欧盘 2 香港盘 */
  private String xjDefaultPosition;

  /** im默认盘口 1 欧盘 2 香港盘 */
  private String imDefaultPosition;

  /** 是否开启体育维护 0:开启 / 1:关闭 */
  private String xjHasEnable;

  /** 是否开启体育维护 0:开启 / 1:关闭 */
  private String btiHasEnable;

  /** bti游戏图(web) */
  private String btiWebImg;

  /** xj游戏图(web) */
  private String xjWebImg;

  /** bti游戏图(h5) */
  private String btiH5Img;

  /** xj游戏图(h5) */
  private String xjH5Img;

  /** 客服地址 */
  private String customerUrl;

  /** 默认盘口 1:欧盘 2:港盘 */
  private Integer handicap;

  /** xj维护开始时间 */
  private Long xjMaintainStartDate;

  /** xj维护结束时间 */
  private Long xjMaintainEndDate;

  /** bti维护开始时间 */
  private Long btiMaintainStartDate;

  /** bti维护结束时间 */
  private Long btiMaintainEndDate;

  /** 是否开启体育维护 0:开启 / 1:关闭 */
  private String shabaHasEnable;

  /** 沙巴维护开始时间 */
  private Long shabaMaintainStartDate;

  /** 沙巴维护结束时间 */
  private Long shabaMaintainEndDate;

  /** 是否开启IM体育维护 0:开启 / 1:关闭 */
  private String imHasEnable;

  /** IM维护开始时间 */
  private Long imMaintainStartDate;

  /** IM维护结束时间 */
  private Long imMaintainEndDate;

  // app活动背景图
  private String appActivityImg;

  // h5活动背景图
  private String h5ActivityImg;

  /** 客服app下载地址 */
  private String customerDownloadUrl;

  /** 场景 */
  private Integer scene;

  /** 排序 */
  private Integer btiSort;

  private Integer xjSort;
  private Integer shabaSort;
  private Integer imSort;
  private Integer squareSort;
  /** 体育开关 */
  private String xjApiEnable;

  private String sbApiEnable;
  private String btiApiEnable;
  private String imApiEnable;
  private String squareEnable;

  private String tenant;
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
}
