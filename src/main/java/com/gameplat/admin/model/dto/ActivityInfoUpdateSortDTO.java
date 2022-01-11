package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 更新活动排序DTO
 *
 * @author kenvin
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("更新活动排序DTO")
public class ActivityInfoUpdateSortDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty(value = "编号")
  private Long id;

  @ApiModelProperty(value = "排序")
  private Integer sort;
}
