package com.gameplat.admin.model.vo.activity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 *
 *@author lyq
 *@Description
 *@date 2020年6月16日 下午2:09:53
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MemberRedPacketConditionVO implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

    @JsonSerialize(using= ToStringSerializer.class)
	@ApiModelProperty(value = "主键")
    private Long conditionId;

    @JsonSerialize(using= ToStringSerializer.class)
    @ApiModelProperty(value = "红包id")
    private Long redPacketId;

    @ApiModelProperty(value = "充值金额")
    private Integer topUpMoney;

    @ApiModelProperty(value = "抽奖次数")
    private Integer drawNum;

    @ApiModelProperty(value = "红包雨最小金额")
    private Integer packetMinMoney;

    @ApiModelProperty(value = "红包雨最大金额")
    private Integer packetMaxMoney;
}
