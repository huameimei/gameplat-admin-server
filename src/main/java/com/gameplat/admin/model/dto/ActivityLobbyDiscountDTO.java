package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 活动大厅打折信息
 * @Author: lyq
 * @Date: 2020/8/20 14:28
 * @Description:
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "ActivityLobbyDiscountDTO", description = "活动大厅打折信息")
public class ActivityLobbyDiscountDTO implements Serializable {

    private static final long serialVersionUID = 2027127567683724890L;

    @ApiModelProperty(value = "大厅优惠id")
    private Long lobbyDiscountId;

    @ApiModelProperty(value = "活动大厅id")
    private Long lobbyId;

    @ApiModelProperty(value = "优惠url")
    private String discountUrl;

    @ApiModelProperty(value = "目标值")
    private Integer targetValue;

    @ApiModelProperty(value = "赠送值")
    private Integer presenterValue;

    @ApiModelProperty(value = "赠送打码")
    private BigDecimal presenterDml;

    @ApiModelProperty(value = "提现打码")
    private Integer withdrawDml;

}
