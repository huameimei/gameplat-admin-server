package com.gameplat.admin.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lily
 * @description
 * @date 2022/1/15
 */
@Data
public class MemberGrowthBannerVO implements Serializable {
  @ApiModelProperty(value = "主键")
  private Long id;

  @ApiModelProperty(value = "终端: 0 WEB  1 H5  2 ANDRIOD  3 IOS")
  private Integer cilentType;

  @ApiModelProperty(value = "页面使用位置: 0 H5 ANDRIOD IOS 对应福利中心轮播   2 对应VIP详情轮播    PC 1对应轮播")
  private Integer areaType;

  @ApiModelProperty(value = "路径")
  private String src;

  @ApiModelProperty(value = "排序值")
  private Integer sort;

  @ApiModelProperty(value = "创建人")
  private String createBy;

  @ApiModelProperty(value = "创建时间")
  private Date createTime;

  @ApiModelProperty(value = "修改人")
  private String updateBy;

  @ApiModelProperty(value = "修改时间")
  private Date updateTime;

  @ApiModelProperty(value = "备注")
  private String remark;
}
