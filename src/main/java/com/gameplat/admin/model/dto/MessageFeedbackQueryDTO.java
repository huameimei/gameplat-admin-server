package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 反馈消息查询DTO
 *
 * @author kenvin
 */
@Data
public class MessageFeedbackQueryDTO implements Serializable {

  /** 反馈标题 */
  @Schema(description = "反馈标题")
  private String title;

  /** 是否已读，0未读 1已读 */
  @Schema(description = "是否已读，0未读 1已读")
  private Integer isRead;

  /** 信件类别(0:普通信件,1:活动信件,2:回复信件,3:系统消息) */
  @Schema(description = "信件类别(0:普通信件,1:活动信件,2:回复信件,3:系统消息)")
  private Integer type;

  @Schema(description = "开始时间")
  private String beginTime;

  @Schema(description = "结束时间")
  private String endTime;
}
