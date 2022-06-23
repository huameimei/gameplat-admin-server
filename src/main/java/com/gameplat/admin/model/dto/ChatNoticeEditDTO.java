package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lily
 * @description
 * @date 2022/2/9
 */
@Data
public class ChatNoticeEditDTO implements Serializable {

  @Schema(description = "主键")
  private Long id;

  @Schema(description = "公告标题")
  private String noticeTitle;

  @Schema(description = "公告内容")
  private String noticeContent;

  @Schema(description = "排序")
  private Integer sort;

  @Schema(description = "起始有效时间")
  private Long beginDate;

  @Schema(description = "截止有效时间")
  private Long endDate;

  @Schema(description = "状态")
  private Integer status;
}
