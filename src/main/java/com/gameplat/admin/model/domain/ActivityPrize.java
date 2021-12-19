package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.*;
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
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("activity_prize")
public class ActivityPrize implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 奖品id
     */
    private Long activityPrizeId;

    /**
     * 活动id
     */
    private Long activityId;

    /**
     * 活动类型（1红包雨，2转盘）
     */
    private Integer type;

    /**
     * 中奖概率
     */
    private Integer prizeChance;

    /**
     * 奖品库存
     */
    private Integer prizeRepertory;

    /**
     * 赠送数量
     */
    private Integer giveAmount;

    /**
     * 一批次发放总数量
     */
    private Integer onceTotalNum;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否组合（0否，1是）
     */
    private Integer isGroup;

    /**
     * 活动组合id
     */
    private Long groupId;
}
