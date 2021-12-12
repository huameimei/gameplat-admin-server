package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 活动资格检测DTO
 *
 * @Author: kenvin
 * @Description:
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ActivityQualificationCheckDTO implements Serializable {

    private static final long serialVersionUID = -3594282509149807621L;

    @ApiModelProperty(value = "资格id")
    private Long qualificationId;

    @ApiModelProperty(value = "活动名称")
    private String activityName;

    @ApiModelProperty(value = "活动类型（1 活动大厅，2 红包雨，3 转盘）")
    private Integer activityType;

    @ApiModelProperty(value = "活动ID")
    private Long activityId;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "会员账号")
    private String username;

    @ApiModelProperty(value = "申请时间")
    private Date applyTime;

    @ApiModelProperty(value = "申请开始时间")
    private String applyStartTime;

    @ApiModelProperty(value = "申请结束时间")
    private String applyEndTime;

    @ApiModelProperty(value = "审核人")
    private String auditPerson;

    @ApiModelProperty(value = "审核时间")
    private Date auditTime;

    @ApiModelProperty(value = "审核备注")
    private String auditRemark;

    @ApiModelProperty(value = "状态（0 无效，1 申请中，2 已审核）")
    private Integer status;

    @ApiModelProperty(value = "活动开始时间")
    private Date activityStartTime;

    @ApiModelProperty(value = "活动结束时间")
    private Date activityEndTime;

    @ApiModelProperty(value = "资格状态（0 禁用，1 启用）")
    private Integer qualificationStatus;

    @ApiModelProperty(value = "截止时间")
    private Date abortTime;

    @ApiModelProperty(value = "总抽奖次数")
    private Integer drawNum;

    @ApiModelProperty(value = "使用次数")
    private Integer employNum;

    @ApiModelProperty(value = "最小金额")
    private Integer minMoney;

    @ApiModelProperty(value = "最大金额")
    private Integer maxMoney;

    @ApiModelProperty(value = "删除（0 已删除，1 未删除）")
    private Integer deleteFlag;

    @ApiModelProperty(value = "使用时间")
    private Date employTime;

    @ApiModelProperty(value = "统计项目（1 累计充值金额，2 累计充值天数，3 连续充值天数，4 单日首充金额，5 首充金额）")
    private Integer statisItem;

    @ApiModelProperty(value = "提现打码量")
    private Integer withdrawDml;

    @ApiModelProperty(value = "奖励详情")
    private String awardDetail;

    @ApiModelProperty(value = "领取方式（1 直接发放，2 福利中心）")
    private Integer getWay;

    @ApiModelProperty(value = "页面大小")
    private Integer pageSize;

    @ApiModelProperty(value = "第几页")
    private Integer pageNum;

    @ApiModelProperty(value = "统计开始时间")
    private Date statisStartTime;

    @ApiModelProperty(value = "统计结束时间")
    private Date statisEndTime;

}
