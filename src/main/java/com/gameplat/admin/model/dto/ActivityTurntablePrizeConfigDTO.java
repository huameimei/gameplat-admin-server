package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 活动转盘奖品配置DTO
 * </p>
 *
 * @author kenvin
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "活动转盘奖品配置DTO", description = "活动转盘奖品配置")
public class ActivityTurntablePrizeConfigDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("奖项ID")
    private Integer prizeId;

    @ApiModelProperty("转盘角度最小值")
    private String min;

    @ApiModelProperty("转盘角度最大值")
    private String max;

    @ApiModelProperty("奖项名称")
    private String prizeName;

    @ApiModelProperty("奖金")
    private Double prizeMoney;

    @ApiModelProperty("中奖名额")
    private Integer quantity;

    @ApiModelProperty("奖项描述")
    private String prizeDesc;

    @ApiModelProperty("中将概率")
    private Double probability;

}
