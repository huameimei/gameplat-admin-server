package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author lily
 * @description 会员俸禄派发修改详情奖励金额入参
 * @date 2021/11/27
 */

@Data
public class MemberWealDetailEditDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "福利表主键")
    private Long id;

    @ApiModelProperty(value = "派发金额")
    private BigDecimal rewordAmount;
}
