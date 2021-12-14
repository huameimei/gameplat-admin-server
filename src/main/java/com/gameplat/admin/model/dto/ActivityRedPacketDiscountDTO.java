package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 红包雨
 * </p>
 *
 * @author 沙漠
 * @since 2020-05-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "ActivityRedPacket优惠查询对象", description = "红包雨")
public class ActivityRedPacketDiscountDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "红包类型（1红包雨，2运营红包）")
    private Integer type;

    @ApiModelProperty(value = "红包雨id/活动id")
    private Long id;

}
