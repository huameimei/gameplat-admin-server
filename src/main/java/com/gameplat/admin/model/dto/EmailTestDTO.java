package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
public class EmailTestDTO implements Serializable {

  @Schema(description = "收件人")
  private String to;

  @Schema(description = "邮件标题")
  private String subject;

  @Schema(description = "邮件内容")
  private String content;
}
