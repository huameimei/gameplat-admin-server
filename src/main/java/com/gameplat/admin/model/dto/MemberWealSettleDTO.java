package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lily
 * @description 福利结算入参
 * @date 2021/11/22
 */
@Data
public class MemberWealSettleDTO implements Serializable {

  @Schema(description = "主键")
  private Long id;
}
