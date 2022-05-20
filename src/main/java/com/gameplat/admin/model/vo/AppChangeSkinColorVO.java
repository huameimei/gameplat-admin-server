package com.gameplat.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * app换服色值
 *
 * @author james
 */
@Data
public class AppChangeSkinColorVO implements Serializable {

  @Schema(description = "主题颜色选中背景")
  private String themeSelectedBackgroundColor;

  @Schema(description = "主题颜色非选中背景")
  private String themeUnselectedBackgroundColor;

  @Schema(description = "主题颜色选中字体色值")
  private String themeSelectedBackgroundFontColor;

  @Schema(description = "主题颜色非选中字体色值")
  private String themeUnselectedBackgroundFontColor;

  @Schema(description = "常规字体主要色值")
  private String regularFontMainColor;

  @Schema(description = "常规字体次要色值")
  private String regularFontSecondaryColor;

  @Schema(description = "提示文字色值")
  private String hintTextColor;

  @Schema(description = "一级背景色值")
  private String primaryBackgroundColor;

  @Schema(description = "二级背景色值")
  private String secondaryBackgroundColor;

  @Schema(description = "三级背景色值")
  private String thirdBackgroundColor;

  @Schema(description = "分割线色值")
  private String splitLineColor;

  @Schema(description = "通用固定")
  private String universalFixed;
}
