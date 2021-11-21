package com.gameplat.admin.model.dto.activity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: lyq
 * @Date: 2020/8/20 14:28
 * @Description:
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MemberLobbyDiscountDTO implements Serializable {

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
