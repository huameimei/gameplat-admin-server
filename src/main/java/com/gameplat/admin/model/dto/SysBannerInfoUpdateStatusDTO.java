package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 更新banner状态
 *
 * @author kenvin
 */
@Data
@ApiModel("更新banner状态DTO")
public class SysBannerInfoUpdateStatusDTO implements Serializable {

  @NotNull(message = "ID不能为空")
  @Min(value = 1, message = "ID必须大于0")
  @ApiModelProperty("主键ID")
  private Long id;

  @NotNull(message = "状态不能为空")
  @ApiModelProperty("状态")
  private Integer status;
}
