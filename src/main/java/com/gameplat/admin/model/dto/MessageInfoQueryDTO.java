package com.gameplat.admin.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 消息查询DTO
 *
 * @author admin
 */
@Data
public class MessageInfoQueryDTO {

  @Schema(description = "消息标题")
  private String title;

  @Schema(description = "消息内容")
  private String content;

  @Schema(description = "状态：0 过期,1 有效")
  private Integer status;

  @Schema(description = "语言种类")
  private String language;

  @Schema(description = "开始时间")
  @JsonFormat(pattern = "yyyy-MM-dd")
  private String beginTime;

  @Schema(description = "结束时间")
  @JsonFormat(pattern = "yyyy-MM-dd")
  private String endTime;

  @Schema(description = "消息类型：2 公告  3 个人消息")
  private Integer type;
}
