package com.gameplat.admin.model.vo.activity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dozer.Mapping;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 首充优惠表
 * </p>
 *
 * @author 沙漠
 * @since 2020-05-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MemberChargeDiscountVO implements Serializable {

    private static final long serialVersionUID=1L;

    @JsonSerialize(using= ToStringSerializer.class)
    @ApiModelProperty(value = "主键")
    @Mapping(value = "discountId")
    private Long id;

    @JsonSerialize(using= ToStringSerializer.class)
    @ApiModelProperty(value = "每日首冲id")
    private Long chargeId;

    @ApiModelProperty(value = "充值金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "图片")
    private String img;

    @ApiModelProperty(value = "优惠（打折或者赠送数量）")
    private BigDecimal discountNum;

    @ApiModelProperty(value = "是否组合（0否，1是）")
    private Integer isGroup;

    @ApiModelProperty(value = "活动组合id")
    private Integer groupId;

}
