package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lily
 * @description 会员权益
 * @date 2022/1/15
 */
@Data
public class MemberWealConfigEditDTO implements Serializable {

  @Schema(description = "主键")
  private Long id;

  @Schema(description = "权益名称")
  private String name;

  @Schema(description = "最低专享等级")
  private Integer level;

  @Schema(description = "未开启描述")
  private String turnDownDesc;

  @Schema(description = "开启描述")
  private String turnUpDesc;

  @Schema(description = "排序值")
  private Integer sort;

  @Schema(description = "是否显示金额 0:不显示 38:晋级礼金金额 36:生日礼金金额 39:每月红包金额")
  private Integer type;

  @Schema(description = "H5权益图标")
  private String image;

  @Schema(description = "web端权益图标")
  private String webImage;

  @Schema(description = "备注")
  private String remark;

  @Schema(description = "语种：zh-CN   en-US   in-ID   th-TH   vi-VN")
  private String language;
}
