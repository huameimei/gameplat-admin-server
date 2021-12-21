package com.gameplat.admin.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.gameplat.admin.util.Date2LongSerializerUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 活动资格VO
 *
 * @Author: whh
 * @Date: 2020/8/25 15:48
 * @Description: 资格检测 VO
 */
@Data
public class ActivityQualificationVO {

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "主键ID")
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "活动名称")
    private String activityName;

    @ApiModelProperty(value = "活动类型（1 活动大厅，2 红包雨，3 转盘）")
    private Integer activityType;

    @ApiModelProperty(value = "活动ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long activityId;

    @ApiModelProperty(value = "会员账号")
    private String username;

    @ApiModelProperty(value = "申请时间")
    @JsonSerialize(using = Date2LongSerializerUtils.class)
    private Date applyTime;

    @ApiModelProperty(value = "审核人")
    private String auditPerson;

    @ApiModelProperty(value = "审核备注")
    private String auditRemark;

    @ApiModelProperty(value = "状态（0 无效，1 申请中，2 已审核）")
    private Integer status;

    @ApiModelProperty(value = "资格状态（0 禁用，1 启用）")
    private Integer qualificationStatus;

    @ApiModelProperty(value = "用户状态 0:非正常 1:正常")
    private Integer userStatus;

    @ApiModelProperty(value = "用户充值层级")
    private String rank;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "统计项目（1 累计充值金额，2 累计充值天数，3 连续充值天数，4 单日首充金额，5 首充金额）")
    private Integer statisItem;

    @ApiModelProperty(value = "提现打码量")
    private Integer withdrawDml;

    @ApiModelProperty(value = "奖励详情")
    private String awardDetail;

    @ApiModelProperty(value = "领取方式（1 直接发放，2 福利中心）")
    private Integer getWay;

    @ApiModelProperty(value = "统计开始时间")
    @JsonSerialize(using = Date2LongSerializerUtils.class)
    private Date statisStartTime;

    @ApiModelProperty(value = "统计结束时间")
    @JsonSerialize(using = Date2LongSerializerUtils.class)
    private Date statisEndTime;

    @ApiModelProperty(value = "审核时间")
    @JsonSerialize(using = Date2LongSerializerUtils.class)
    private Date auditTime;

    @ApiModelProperty(value = "活动开始时间")
    @JsonSerialize(using = Date2LongSerializerUtils.class)
    private Date activityStartTime;

    @ApiModelProperty(value = "活动结束时间")
    @JsonSerialize(using = Date2LongSerializerUtils.class)
    private Date activityEndTime;

    @ApiModelProperty(value = "更新时间")
    @JsonSerialize(using = Date2LongSerializerUtils.class)
    private Date updateTime;

    @ApiModelProperty(value = "截止时间")
    @JsonSerialize(using = Date2LongSerializerUtils.class)
    private Date abortTime;

    @ApiModelProperty(value = "使用次数")
    private Integer employNum;

    @ApiModelProperty(value = "最小金额")
    private Integer minMoney;

    @ApiModelProperty(value = "最大金额")
    private Integer maxMoney;

    @ApiModelProperty(value = "删除（0 已删除，1 未删除）")
    private Integer deleteFlag;

    @ApiModelProperty(value = "使用时间")
    @JsonSerialize(using = Date2LongSerializerUtils.class)
    private Date employTime;

    @ApiModelProperty(value = "与活动派发关联id")
    private String qualificationActivityId;

    @ApiModelProperty(value = "唯一标识")
    private String soleIdentifier;

}
