package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author lyq @Description 首充优惠DTO
 * @date 2020年6月9日 下午4:32:50
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ActivityChargeDiscountDTO implements Serializable {

  /** */
  private static final long serialVersionUID = 1L;

  @Schema(description = "主键")
  private Long discountId;

  @Schema(description = "每日首冲id")
  private Long chargeId;

  @Schema(description = "是否组合（0否，1是）")
  private Integer isGroup;

  @Schema(description = "活动组合id")
  private Integer groupId;
}
