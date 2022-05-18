package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 红包雨
 *
 * @author 沙漠
 * @since 2020-05-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ActivityRedPacketDiscountDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "红包类型（1红包雨，2运营红包）")
  private Integer type;

  @Schema(description = "红包雨id/活动id")
  private Long id;
}
