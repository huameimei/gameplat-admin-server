package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author: lyq
 * @Date: 2020/8/20 11:45
 * @Description:
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ActivityDistributeDTO implements Serializable {

    private static final long serialVersionUID = 4142676336119312814L;

    @ApiModelProperty(value = "页面大小")
    private Integer pageSize;

    @ApiModelProperty(value = "第几页")
    private Integer pageNum;

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

    @ApiModelProperty(value = "资格审核时间")
    private Date applyTime;

    @ApiModelProperty(value = "资格审核开始时间")
    private String applyStartTime;

    @ApiModelProperty(value = "资格审核结束时间")
    private String applyEndTime;

    @ApiModelProperty(value = "优惠金额")
    private Integer discountsMoney;

    @ApiModelProperty(value = "优惠奖品")
    private String discountsPrize;

    @ApiModelProperty(value = "结算时间")
    private Date settlementTime;

    @ApiModelProperty(value = "状态（1 结算中，2 已结算） ")
    private Integer status;

    @ApiModelProperty(value = "截止时间")
    private Date abortTime;

    @ApiModelProperty(value = "删除（0 已删除，1 未删除）")
    private Integer deleteFlag;

    @ApiModelProperty(value = "会员充值层级")
    private Integer memberPayLevel;

    @ApiModelProperty(value = "状态（1 结算中，2 已结算, 3過期） ")
    private List<String> statusList;

    @ApiModelProperty(value = "是否失效（0 失效，1 未失效）")
    private Integer disabled;

    @ApiModelProperty(value = "过期时间")
    private String expired;

    @ApiModelProperty(value = "领取方式（1 直接发放，2 福利中心）")
    private Integer getWay;

    @ApiModelProperty(value = "提现打码量")
    private Integer withdrawDml;

    @ApiModelProperty(value = "统计开始时间")
    private Date statisStartTime;

    @ApiModelProperty(value = "统计结束时间")
    private Date statisEndTime;


}
