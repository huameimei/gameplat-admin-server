package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author zhuzi
 * @program: open-live-platform
 * @description: 直播流量查询
 * @date 2020-08-31 14:06:06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "AnchorParam", description = "直播流量查询入参")
public class LiveDomainParamDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "需要查询的直播域名")
    private String domain;
    @ApiModelProperty(value = "获取数据起始时间点，最长可查询90天内的数据，格式：yyyy-MM-dd HH:mm:ss 默认本月第一天0点（如：2020-12-01 00:00:00）")
    private String startTime;
    @ApiModelProperty(value = "结束时间需大于起始时间,格式：yyyy-MM-dd HH:mm:ss 默认当期时间")
    private String endTime;
    @ApiModelProperty(value = "查询数据的时间粒度，支持300, 3600和86400秒")
    private String interval;
    @ApiModelProperty(value = "日期范围类型 0 单月 1 上月")
    private String dateType;
    @ApiModelProperty(value = "zh-CN:中文;en-US:英文")
    private String country;
}
