package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 活动资格管理
 *
 * @author kenvin
 * @Description 实体层
 */
@TableName("activity_qualification")
@Data
@EqualsAndHashCode(callSuper = false)
public class ActivityQualification implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 资格id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

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
     * 审核人
     */
    private String auditPerson;

    /**
     * 审核时间
     */
    private Date auditTime;

    /**
     * 审核备注
     */
    private String auditRemark;

    /**
     * 状态（0 无效，1 申请中，2 已审核）
     */
    private Integer status;

    /**
     * 活动开始时间
     */
    private Date activityStartTime;

    /**
     * 活动结束时间
     */
    private Date activityEndTime;

    /**
     * 统计开始时间
     */
    private Date statisStartTime;

    /**
     * 统计结束时间
     */
    private Date statisEndTime;

    /**
     * 删除（0 已删除，1 未删除）
     */
    private Integer deleteFlag;

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
     * 总抽奖次数
     */
    private Integer drawNum;

    /**
     * 使用次数
     */
    private Integer employNum;

    /**
     * 最小金额
     */
    private Integer minMoney;

    /**
     * 最大金额
     */
    private Integer maxMoney;

    /**
     * 资格状态（0 禁用，1 启用）
     */
    private Integer qualificationStatus;

    /**
     * 使用时间
     */
    private Date employTime;

    /**
     * 与活动派发关联id
     */
    private String qualificationActivityId;

    /**
     * 唯一标识
     */
    private String soleIdentifier;

    /**
     * 是否禁用，1:启用，0:禁用
     */
    private Integer disable;

    /**
     * 统计项目（1 累计充值金额，2 累计充值天数，3 连续充值天数，4 单日首充金额，5 首充金额）
     */
    private Integer statisItem;

    /**
     * 提现打码量
     */
    private Integer withdrawDml;

    /**
     * 奖励详情
     */
    private String awardDetail;

    /**
     * 领取方式（1 直接发放，2 福利中心）
     */
    private Integer getWay;


}
