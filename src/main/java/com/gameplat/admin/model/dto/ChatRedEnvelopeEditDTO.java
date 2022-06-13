package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
  @Schema(description = "id")
  private Integer id;

  @NotNull(message = "启用状态不能为空！")
  @Schema(description = "禁止_启用1:启用，0：禁用")
  private Integer open;
}
