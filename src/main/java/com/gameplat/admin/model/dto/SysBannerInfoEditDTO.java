package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * banner信息DTO
 *
 * @author admin
 */
@Data
public class SysBannerInfoEditDTO implements Serializable {

  /** 主键 */
  @NotNull(message = "ID不能为空")
  @Min(value = 1, message = "ID必须大于0")
  @Schema(description = "主键ID")
  private Long id;

  /** banner类型 */
  @NotNull(message = "banner大类不能为空")
  @Schema(description = "banner大类，1 体育banner配置，2 彩票banner配置")
  private Integer type;

  /** banner类型 */
  //    @NotNull(message = "banner类型不能为空")
  @Schema(description = "banner类型")
  private Integer bannerType;

  /** banner子类型 */
  @Schema(description = "banner子类型")
  private Integer childType;

  /** banner子类型名称 */
  @Schema(description = "banner子类型名称")
  private String childName;

  /** pc端图片地址 */
  @NotBlank(message = "pc端图片地址不能为空")
  @Schema(description = "pc端图片地址")
  private String pcPicUrl;

  /** app端图片地址 */
  @Schema(description = "app端图片地址")
  private String appPicUrl;

  /** 状态 */
  @Schema(description = "状态（0禁用 1启用）")
  private Integer status;

  /** 备注 */
  @Schema(description = "备注")
  private String remark;

  /** 语种 */
  @Schema(description = "语种")
  private String language;

  @Schema(description = "跳转地址")
  private String jumpUrl;

  @Schema(description = "游戏类别")
  private String gameKind;

  @Schema(description = "关联游戏")
  private String gameCode;

  @Schema(description = "排序")
  private Integer sort;

  /**
   * 展示位置  0 上 1下
   */
  private Integer location;

  /** banner标题 */
  @Schema(description = "banner标题")
  private String title;
}
