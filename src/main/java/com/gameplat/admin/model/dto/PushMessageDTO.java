package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 个人消息
 *
 * @author kenvin
 */
@Data
public class PushMessageDTO {

    @ApiModelProperty(value = "推送消息的标题")
    private String messageTitle;

    @ApiModelProperty(value = "接收人消息的账号")
    private String userAccount;

    @ApiModelProperty(value = "是否已读: 0:未读; 1:已读")
    private Integer readStatus;

    @ApiModelProperty(value = "是否即时消息,0:否,1:是")
    public Integer immediateFlag;

    @ApiModelProperty(value = "开始推送时间")
    private String beginDate;

    @ApiModelProperty(value = "结束推送时间")
    private String endDate;

}
