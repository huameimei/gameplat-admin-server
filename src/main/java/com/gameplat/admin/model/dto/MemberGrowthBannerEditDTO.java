package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author lily
 * @description
 * @date 2022/1/15
 */
@Data
public class MemberGrowthBannerEditDTO implements Serializable {

  @ApiModelProperty(value = "主键")
  @NotNull(message = "id不能为空")
  private Long id;

  @ApiModelProperty(value = "终端: 0 WEB  1 H5  2 ANDRIOD  3 IOS")
  private Integer cilentType;

  @ApiModelProperty(value = "匹配界面: 0 H5 ANDRIOD IOS 对应福利中心轮播   2 对应VIP详情轮播    PC 1对应轮播")
  private Integer areaType;

  @ApiModelProperty(value = "路径")
  private String src;

  @ApiModelProperty(value = "排序值")
  private Integer sort;
}
