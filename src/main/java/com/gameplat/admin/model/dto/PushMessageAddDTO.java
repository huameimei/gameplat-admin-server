package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 个人消息新增DTO
 */
@Data
public class PushMessageAddDTO {

    @ApiModelProperty(value = "推送消息的标题")
    private String messageTitle;

    @ApiModelProperty(value = "推送消息的内容")
    private String messageContent;

    @ApiModelProperty(value = "接收人消息的账号，会员账号")
    private String userAccount;

    @ApiModelProperty(value = "推送范围选择代理线时，代理账号")
    private String agentAccount;

    @ApiModelProperty(value = "是否已读: 0-未读; 1-已读")
    private Integer readStatus;

    @ApiModelProperty(value = "消息推送范围：1-部分会员，2-所有会有，3-在线会员,4-指定层级,5-代理线")
    private Integer userRange;

    @ApiModelProperty(value = "显示类型")
    private Integer showType;

    @ApiModelProperty("充值层级,保存多个层级的值")
    private String[] userLevel;

    @ApiModelProperty(value = "是否即时消息,0:否,1:是")
    public Integer immediateFlag;

    @ApiModelProperty(value = "发送人消息的账号")
    public String createBy;

}
