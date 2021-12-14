package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 活动奖品表
 * </p>
 *
 * @since 2020-06-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("activity_prize")
@ApiModel(value = "ActivityPrize对象", description = "活动奖品表")
public class ActivityPrize implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
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

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "是否组合（0否，1是）")
    private Integer isGroup;

    @ApiModelProperty(value = "活动组合id")
    private Long groupId;
}
