package com.gameplat.admin.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;

/**
 * @Author: whh
 * @Date: 2020/8/21 16:53
 * @Description: 定时查询 订单表参数
 */
@Data
public class ActivitySendVO {

    @ApiModelProperty(value = "活动Id")
    private String activityId;

    @ApiModelProperty(value = "开始时间")
    private Date startDate;

    @ApiModelProperty(value = "结束时间")
    private Date endDate;

    @JsonSerialize(using= ToStringSerializer.class)
    @ApiModelProperty(value = "用户Id")
    private Long userId;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "首充金额")
    private String firstAmount;

    @ApiModelProperty(value = "用户充值金额")
    private String amount;

    @ApiModelProperty(value = "用户充值金额(用于计算)")
    private Double cumulativeAmount;

    @ApiModelProperty(value = "充值时间")
    private Date payTime;
}
