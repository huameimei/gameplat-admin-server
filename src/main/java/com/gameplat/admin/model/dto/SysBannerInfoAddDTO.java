package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
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
  @NotNull(message = "banner类型不能为空")
  @ApiModelProperty("banner类型")
  private Integer bannerType;

  /** banner子类型 */
  @ApiModelProperty("banner子类型")
  private Integer childType;

  /** banner子类型名称 */
  @ApiModelProperty("banner子类型名称")
  private String childName;

  /** pc端图片地址 */
  @NotBlank(message = "pc端图片地址不能为空")
  @ApiModelProperty("pc端图片地址")
  private String pcPicUrl;

  /** app端图片地址 */
  @NotBlank(message = "app端图片地址不能为空")
  @ApiModelProperty("app端图片地址")
  private String appPicUrl;

  /** 状态 */
  @NotNull(message = "状态不能为空")
  @ApiModelProperty("状态")
  private Integer status;

  /** 备注 */
  @ApiModelProperty("备注")
  private String remark;

  /** 语种 */
  @NotNull(message = "语种不能为空")
  @ApiModelProperty("语种")
  private String language;

  @ApiModelProperty("跳转地址")
  private String jumpUrl;

  @ApiModelProperty("游戏类别")
  private String gameKind;

  @ApiModelProperty("关联游戏")
  private String gameCode;
}
