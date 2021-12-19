package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 活动分发
 *
 * @author kenvin
 * @Description 实体层
 * @date 2020-08-20 11:30:39
 */
@Data
@TableName("activity_distribute")
@EqualsAndHashCode(callSuper = false)
public class ActivityDistribute implements Serializable {

    private static final long serialVersionUID = -1005615158531421103L;

    /**
     * 派发id
     */
    @TableId(value = "distribute_id", type = IdType.AUTO)
    private Long distributeId;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 活动类型（1 活动大厅，2 红包雨，3 转盘）
     */
    private Integer activityType;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 会员账号
     */
    private String username;

    /**
     * 申请时间
     */
    private Date applyTime;

    /**
     * 优惠金额
     */
    private BigDecimal discountsMoney;

    /**
     * 优惠奖品
     */
    private String discountsPrize;

    /**
     * 结算时间
     */
    private Date settlementTime;

    /**
     * 状态（1 结算中，2 已结算）
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

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
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 目标金额
     */
    private Integer targetMoney;

    /**
     * 是否失效（1 失效，0 未失效）
     */
    private Integer disabled;

    /**
     * 删除（0 已删除，1 未删除）
     */
    private Integer deleteFlag;

    /**
     * 会员充值层级
     */
    private Integer memberPayLevel;

    /**
     * 与资格管理关联id
     */
    private String qualificationActivityId;

    /**
     * 唯一标识
     */
    private String soleIdentifier;

    /**
     * 统计项目（1 累计充值金额，2 累计充值天数，3 连续充值天数，4 单日首充金额，5 首充金额）
     */
    private Integer statisItem;

    /**
     * 提现打码量
     */
    private Integer withdrawDml;

    /**
     * 统计开始时间
     */
    private Date statisStartTime;

    /**
     * 统计结束时间
     */
    private Date statisEndTime;

    /**
     * 领取方式（1 直接发放，2 福利中心）
     */
    private Integer getWay;

}
