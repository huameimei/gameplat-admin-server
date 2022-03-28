package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
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

  @NotNull(message = "ID不能为空")
  @Min(value = 1, message = "ID必须大于0")
  @ApiModelProperty(value = "编号ID")
  private Long id;

  @NotNull(message = "排序不能为空")
  @Min(value = 1, message = "排序必须大于0")
  @ApiModelProperty(value = "排序")
  private Integer sort;
}
