package com.gameplat.admin.model.domain.activity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * @author lyq
 * @Description 实体层
 * @date 2020-08-20 11:30:39
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ActivityDistribute implements Serializable {

    private static final long serialVersionUID = -1005615158531421103L;

    @ApiModelProperty(value = "派发id集合")
	private List<Long> distributeIds;

    @ApiModelProperty(value = "派发id")
    private Long distributeId;

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

    @ApiModelProperty(value = "优惠金额")
    private BigDecimal discountsMoney;

    @ApiModelProperty(value = "优惠奖品")
    private String discountsPrize;

    @ApiModelProperty(value = "结算时间")
    private Date settlementTime;

    @ApiModelProperty(value = "状态（1 结算中，2 已结算） ")
    private Integer status;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "目标金额")
    private Integer targetMoney;

    @ApiModelProperty(value = "是否失效（0 失效，1 未失效）")
    private Integer disabled;

    @ApiModelProperty(value = "截止时间")
    private Date abortTime;

    @ApiModelProperty(value = "删除（0 已删除，1 未删除）")
    private Integer deleteFlag;

    @ApiModelProperty(value = "会员充值层级")
    private Integer memberPayLevel;

    @ApiModelProperty(value = "与资格管理关联id")
    private String qualificationActivityId;

    @ApiModelProperty(value = "唯一标识")
    private String soleIdentifier;

    private String expired;

    @ApiModelProperty(value = "状态（1 结算中，2 已结算, 3過期） ")
    private List<String> statusList;

    @ApiModelProperty(value = "统计项目（1 累计充值金额，2 累计充值天数，3 连续充值天数，4 单日首充金额，5 首充金额）")
    private Integer statisItem;

    @ApiModelProperty(value = "提现打码量")
    private Integer withdrawDml;

    @ApiModelProperty(value = "统计开始时间")
    private Date statisStartTime;

    @ApiModelProperty(value = "统计结束时间")
    private Date statisEndTime;

    @ApiModelProperty(value = "领取方式（1 直接发放，2 福利中心）")
    private Integer getWay;

}
