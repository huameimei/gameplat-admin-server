package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel("区号设置新增DTO")
public class SmsAreaEditDTO implements Serializable {

  @ApiModelProperty("主键ID")
  @Min(value = 1, message = "编号必须大于0")
  @NotNull(message = "编号不能为空")
  private Long id;

  @ApiModelProperty("编码")
  @NotEmpty(message = "区号编码不能为空")
  private String code;

  @ApiModelProperty("国家/地区")
  @NotEmpty(message = "国家名称不能为空")
  private String name;

  @ApiModelProperty("状态 0 禁用 1 启用")
  @NotEmpty(message = "状态不能为空")
  private String status;
}
