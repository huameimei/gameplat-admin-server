package com.gameplat.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
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

  @Schema(description = "主键")
  private Long id;

  @Schema(description = "终端: 0 WEB  1 H5  2 ANDRIOD  3 IOS")
  private Integer cilentType;

  @Schema(description = "页面使用位置: 0 H5 ANDRIOD IOS 对应福利中心轮播   2 对应VIP详情轮播    PC 1对应轮播")
  private Integer areaType;

  @Schema(description = "路径")
  private String src;

  @Schema(description = "排序值")
  private Integer sort;

  @Schema(description = "创建人")
  private String createBy;

  @Schema(description = "创建时间")
  private Date createTime;

  @Schema(description = "修改人")
  private String updateBy;

  @Schema(description = "修改时间")
  private Date updateTime;

  @Schema(description = "备注")
  private String remark;
}
