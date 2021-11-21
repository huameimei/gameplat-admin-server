package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 个人消息编辑DTO
 */
@Data
public class PushMessageEditDTO {

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "推送消息的标题")
    private String messageTitle;

    @ApiModelProperty(value = "推送消息的内容")
    private String messageContent;

    @ApiModelProperty(value = "会员ID")
    private Long userId;

    @ApiModelProperty(value = "接收人消息的账号")
    private String userAccount;

    @ApiModelProperty(value = "发送人消息的账号")
    private String createBy;

    @ApiModelProperty(value = "是否已读: 0-未读; 1-已读")
    private Integer readStatus;

    @ApiModelProperty(value = "添加时间")
    private Date createTime;

    @ApiModelProperty(value = "接收者阅读消息的时间")
    private Date readTime;


}
