package com.gameplat.admin.model.dto.activity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Author bhf
 * @Description 活动转盘抽奖交互数据
 * @Date 2020/6/12 18:29
 **/
@Data
@EqualsAndHashCode(callSuper = false)
public class AppActivityLuckDrawDTO implements Serializable {

    /**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "用户编号")
    private Long userId;

    @ApiModelProperty(value = "活动编号")
    private Long activityId;

    @ApiModelProperty(value = "转盘类型（1:1次，10:10次）")
    private Integer turnType;

    @ApiModelProperty(value = "消耗的币类型（1：真棒 2：假币）")
    private Integer currencyType;

    @ApiModelProperty(value = "消耗的币")
    private Integer currency;

    @ApiModelProperty(value = "幸运值")
    private Integer luckyValue;

}
