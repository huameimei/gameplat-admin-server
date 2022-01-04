package com.gameplat.admin.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameplat.admin.util.Date2LongSerializerUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 消息分发到会员列表
 *
 * @author: kenvin
 * @date: 2021/5/1 9:22
 * @desc:
 */
@Data
public class MessageDistributeVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "消息id")
    private Long messageId;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "用户账号")
    private String userAccount;

    @ApiModelProperty(value = "充值层级")
    private Integer rechargeLevel;

    @ApiModelProperty(value = "VIP等级")
    private Integer vipLevel;

    @ApiModelProperty(value = "代理层级")
    private String agentLevel;

    @ApiModelProperty(value = "阅读状态：0 未读  1 已读")
    private Integer readStatus;

    @ApiModelProperty(value = "发送移除标识")
    private Integer sendRemoveFlag;

    @ApiModelProperty(value = "接收移除标识")
    private Integer acceptRemoveFlag;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @JsonSerialize(using = Date2LongSerializerUtils.class)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @JsonSerialize(using = Date2LongSerializerUtils.class)
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
