package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 活动类型更新DTO
 *
 * @author kenvin
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "ActivityTypeUpdateDTO", description = "活动类型更新DTO")
public class ActivityTypeUpdateDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  @NotNull(message = "ID不能为空")
  @Min(value = 1, message = "id必须大于0")
  @ApiModelProperty(value = "编号")
  private Long id;

  @ApiModelProperty(value = "活动类型")
  private String typeCode;

  @ApiModelProperty(value = "活动类型名称")
  private String typeName;

  @ApiModelProperty(value = "备注")
  private String remark;

  @NotNull(message = "排序不能为空")
  @Min(value = 1, message = "排序必须大于0")
  @ApiModelProperty(value = "排序")
  private Integer sort;

  @ApiModelProperty(value = "状态,0 无效,1 有效")
  private Integer typeStatus;

  @ApiModelProperty(value = "浮窗状态,0 无效,1 有效")
  private Integer floatStatus;

  @ApiModelProperty(value = "浮窗logo")
  private String floatLogo;

  @ApiModelProperty(value = "浮窗url")
  private String floatUrl;

  @ApiModelProperty(value = "语言")
  private String language;
}
