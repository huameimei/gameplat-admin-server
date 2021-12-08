package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dozer.Mapping;

/**
 * @author lyq
 * @Description 活动奖品DTO
 * @date 2020年6月9日 下午6:40:09
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ActivityPrizeDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "活动奖品id")
    @Mapping(value = "id")
    private Long id;

    @ApiModelProperty(value = "奖品id")
    private Long activityPrizeId;

    @ApiModelProperty(value = "活动id")
    private Long activityId;

    @ApiModelProperty(value = "活动类型（1红包雨，2转盘）")
    private Integer type;

    @ApiModelProperty(value = "中奖概率")
    private Integer prizeChance;

    @ApiModelProperty(value = "奖品库存")
    private Integer prizeRepertory;

    @ApiModelProperty(value = "赠送数量")
    private Integer giveAmount;

    @ApiModelProperty(value = "一批次发放总数量")
    private Integer onceTotalNum;

    @ApiModelProperty(value = "是否组合（0否，1是）")
    private Integer isGroup;

    @ApiModelProperty(value = "活动组合id")
    private Integer groupId;

}
