package com.gameplat.admin.model.domain.activity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 活动支付
 */
@Data
public class ActivityPay2 implements Serializable {

    private static final long serialVersionUID = 8517200788176172969L;

    private Long id;

    @ApiModelProperty(value = "用户Id")
    private Long userId;

    @ApiModelProperty(value = "活动Id")
    private Long activityId;

    @ApiModelProperty(value = "支付时间")
    private Date payTime;

    @ApiModelProperty(value = "首充金额")
    private String firstAmount;

    @ApiModelProperty(value = "支付金额")
    private String amount;

    @ApiModelProperty(value = "充值类型（1 转账汇款，2 在线支付，3 人工入款）")
    private Integer status;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    private Date createTime;

    private Date updateTime;

    private String orderNo;

}
