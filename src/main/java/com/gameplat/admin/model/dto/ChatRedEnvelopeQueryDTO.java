package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lily
 * @description
 * @date 2022/2/14
 */
@Data
public class ChatRedEnvelopeQueryDTO implements Serializable {

  @Schema(description = "红包名称")
  private String name;

  @Schema(description = "开始发送红包时间(时间戳)")
  private Long startTime;

  @Schema(description = "禁止_启用1:启用，0：禁用")
  private Integer open;
}
