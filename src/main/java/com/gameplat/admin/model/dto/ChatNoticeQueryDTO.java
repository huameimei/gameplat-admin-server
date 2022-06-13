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
public class ChatNoticeQueryDTO implements Serializable {

  @Schema(description = "公告标题")
  private String noticeTitle;

  @Schema(description = "状态")
  private Integer status;
}
