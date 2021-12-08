package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 个人消息删除DTO
 */
@Data
public class PushMessageRemoveDTO {

    @ApiModelProperty(value = "推送消息的标题")
    private String messageTitle;

    @ApiModelProperty(value = "推送消息的内容")
    private String messageContent;

    @ApiModelProperty(value = "是否已读: 0-未读; 1-已读")
    private Integer readStatus;

    @ApiModelProperty(value = "开始推送时间")
    private String beginDate;

    @ApiModelProperty(value = "结束推送时间")
    private String endDate;



}
