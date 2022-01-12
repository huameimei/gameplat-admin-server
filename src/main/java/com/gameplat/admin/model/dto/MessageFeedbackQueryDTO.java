package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 反馈消息查询DTO
 *
 * @author kenvin
 */
@Data
@ApiModel("反馈消息查询DTO")
public class MessageFeedbackQueryDTO implements Serializable {

  /** 反馈标题 */
  @ApiModelProperty("反馈标题")
  private String title;

  /** 是否已读，0未读 1已读 */
  @ApiModelProperty("是否已读，0未读 1已读")
  private Integer isRead;

  /** 信件类别(0:普通信件,1:活动信件,2:回复信件,3:系统消息) */
  @ApiModelProperty("信件类别(0:普通信件,1:活动信件,2:回复信件,3:系统消息)")
  private Integer type;
}
