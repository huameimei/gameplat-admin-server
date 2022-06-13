package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * banner信息DTO
 *
 * @author kenvin
 */
@Data
public class SysBannerInfoAddDTO implements Serializable {

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
  //    @NotBlank(message = "app端图片地址不能为空")
  @Schema(description = "app端图片地址")
  private String appPicUrl;

  /** 状态 */
  @NotNull(message = "状态不能为空")
  @Schema(description = "状态（0禁用 1启用）")
  private Integer status;

  /** 备注 */
  @Schema(description = "备注")
  private String remark;

  /** 语种 */
  //    @NotBlank(message = "语言不能为空")
  @Schema(description = "语言")
  private String language;

  @Schema(description = "跳转地址")
  private String jumpUrl;

  @Schema(description = "相关的游戏配置")
  private String gameConfig;

  @Schema(description = "排序")
  private Integer sort;

  /** 展示位置 0 上 1下 */
  private Integer location;

  /** 标题 */
  @Schema(description = "标题")
  private String title;
}
