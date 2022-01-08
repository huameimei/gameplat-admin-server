package com.gameplat.admin.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gameplat.base.common.util.DateUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 消息分发到会员查询DTO
 *
 * @author: kenvin
 * @date: 2021/5/1 9:22
 * @desc:
 */
@Data
public class MessageDistributeQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "消息id")
    private Long messageId;

    @ApiModelProperty(value = "推送范围")
    private Integer pushRange;

    @ApiModelProperty(value = "会员账号")
    private String userAccount;

    @ApiModelProperty(value = "充值层级/会员层级")
    private Integer rechargeLevel;

    @ApiModelProperty(value = "VIP等级")
    private Integer vipLevel;

    @JsonFormat(pattern = DateUtil.YYYY_MM_DD_HH_MM_SS)
    @ApiModelProperty(value = "开始时间")
    private Date beginTime;

    @JsonFormat(pattern = DateUtil.YYYY_MM_DD_HH_MM_SS)
    @ApiModelProperty(value = "结束时间")
    private Date endTime;


}