package com.gameplat.admin.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameplat.admin.util.Date2LongSerializerUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * banner信息VO
 *
 * @author admin
 */
@Data
public class SysBannerInfoVO implements Serializable {

  /** 主键 */
  @Schema(description = "主键")
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  /** banner大类，1 体育banner配置，2 彩票banner配置 */
  @Schema(description = "banner大类，1 体育banner配置，2 彩票banner配置")
  private Integer type;

  /** banner类型 */
  @Schema(description = "banner类型")
  private Integer bannerType;

  /** banner子类型 */
  @Schema(description = "banner子类型")
  private Integer childType;

  /** banner子类型名称 */
  @Schema(description = "banner子类型名称")
  private String childName;

  /** pc端图片地址 */
  @Schema(description = "pc端图片地址")
  private String pcPicUrl;

  /** app端图片地址 */
  @Schema(description = "app端图片地址")
  private String appPicUrl;

  /** 状态 */
  @Schema(description = "状态（0禁用 1启用）")
  private Integer status;

  /** 排序 */
  @Schema(description = "排序")
  private Integer sort;

  /** 创建时间 */
  @Schema(description = "创建时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date createTime;

  /** 创建人 */
  @Schema(description = "创建人")
  private String createBy;

  /** 更新时间 */
  @Schema(description = "更新时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date updateTime;

  /** 更新人 */
  @Schema(description = "更新人")
  private String updateBy;

  /** 备注 */
  @Schema(description = "备注")
  private String remark;

  /** 语种 */
  @Schema(description = "语种")
  private String language;

  @Schema(description = "跳转地址")
  private String jumpUrl;

  @Schema(description = "相关游戏配置")
  private String gameConfig;

  /** 展示位置 0 上 1下 */
  @Schema(description = "展示位置  0 上 1下")
  private Integer location;

  /** banner标题 */
  @Schema(description = "banner标题")
  private String title;
}
