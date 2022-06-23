package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author lily
 * @description 会员权益
 * @date 2022/1/15
 */
@Data
public class MemberWealConfigAddDTO implements Serializable {

  @Schema(description = "权益名称")
  @NotNull(message = "权益名称不能为空")
  private String name;

  @Schema(description = "最低专享等级")
  @NotNull(message = "最低专享等级不能为空")
  private Integer level;

  @Schema(description = "未开启描述")
  @NotNull(message = "未开启描述不能为空")
  private String turnDownDesc;

  @Schema(description = "开启描述")
  @NotNull(message = "开启描述不能为空")
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
