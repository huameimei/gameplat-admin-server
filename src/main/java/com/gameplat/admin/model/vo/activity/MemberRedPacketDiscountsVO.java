package com.gameplat.admin.model.vo.activity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 *
 *@author lyq
 *@Description
 *@date 2020年6月16日 下午3:18:16
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MemberRedPacketDiscountsVO implements Serializable{/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主键")
    private List<MemberActivityPrizeVO> activityPrize;

    @ApiModelProperty(value = "红包id")
    private List<com.live.cloud.backend.model.activity.vo.MemberRedPacketConditionVO> redPacketCondition;


}
