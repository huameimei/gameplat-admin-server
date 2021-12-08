package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 *@author lyq
 *@Description 首充优惠DTO
 *@date 2020年6月9日 下午4:32:50
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ActivityChargeDiscountDTO implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主键")
    private Long discountId;

    @ApiModelProperty(value = "每日首冲id")
    private Long chargeId;

    @ApiModelProperty(value = "是否组合（0否，1是）")
    private Integer isGroup;

    @ApiModelProperty(value = "活动组合id")
    private Integer groupId;
}
