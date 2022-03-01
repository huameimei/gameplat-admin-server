package com.gameplat.admin.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 消息查询DTO
 *
 * @author admin
 */
@Data
public class MessageInfoQueryDTO {

  @ApiModelProperty(value = "消息标题")
  private String title;

  @ApiModelProperty(value = "消息内容")
  private String content;

  @ApiModelProperty(value = "状态：0 过期,1 有效")
  private Integer status;

  @ApiModelProperty(value = "语言种类")
  private String language;

  @ApiModelProperty(value = "开始时间")
  @JsonFormat(pattern = "yyyy-MM-dd")
  private String beginTime;

  @ApiModelProperty(value = "结束时间")
  @JsonFormat(pattern = "yyyy-MM-dd")
  private String endTime;

  @ApiModelProperty(value = "消息类型：2 公告  3 个人消息")
  private Integer type;
}
