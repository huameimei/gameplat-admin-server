package com.gameplat.admin.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameplat.common.util.I18nSerializerUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 游戏VO
 *
 * @author kenvin
 */
@Data
public class GameVO implements Serializable {

  @Schema(description = "主键ID")
  private Long id;

  @Schema(description = "游戏编码")
  private String gameCode;

  @Schema(description = "游戏名")
  @JsonSerialize(using = I18nSerializerUtils.class)
  private String gameName;

  @Schema(description = "PC图片地址")
  private String pcImgUrl;

  @Schema(description = "APP图片地址")
  private String appImgUrl;

  @Schema(description = "游戏平台")
  private String platformCode;

  @Schema(description = "游戏大类")
  private String gameType;

  @Schema(description = "游戏类别")
  private String gameKind;

  @Schema(description = "是否支持H5 (0：否；1:是)")
  private Integer isH5;

  @Schema(description = "是否支持电脑端(0：否；1:是)")
  private Integer isPc;

  @Schema(description = "游戏排序")
  private Integer sort;

  /** 是否开放(0：否；1:是) */
  private Integer enable;

  /** 是否热门(0：否；1:是) */
  private Integer hot;

  /** 是否外跳(0：否；1:是) */
  private Integer isJump;

  /** 是否竖屏(0：否；1:是) */
  private Integer isVertical;
}
