package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author lily
 * @description
 * @date 2022/2/14
 */
@Data
public class ChatRedEnvelopeEditDTO implements Serializable {

  @NotNull(message = "id不能为空！")
  @ApiModelProperty(value = "id")
  private Integer id;

  @NotNull(message = "启用状态不能为空！")
  @ApiModelProperty(value = "禁止_启用1:启用，0：禁用")
  private Integer open;
}
