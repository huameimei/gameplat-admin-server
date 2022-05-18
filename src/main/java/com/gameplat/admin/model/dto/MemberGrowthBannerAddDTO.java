package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author lily
 * @description
 * @date 2022/1/15
 */
@Data
public class MemberGrowthBannerAddDTO implements Serializable {

  @Schema(description = "终端: 0 WEB  1 H5  2 ANDRIOD  3 IOS")
  @NotNull(message = "终端类型不能为空")
  private Integer cilentType;

  @Schema(description = "页面使用位置: 0 H5 ANDRIOD IOS 对应福利中心轮播   2 对应VIP详情轮播    PC 1对应轮播")
  @NotNull(message = "页面使用位置类型不能为空")
  private Integer areaType;

  @Schema(description = "路径")
  private String src;

  @Schema(description = "排序值")
  private Integer sort;

  @Schema(description = "备注")
  private String remark;
}
