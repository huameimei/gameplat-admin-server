package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel
public class EmailTestDTO implements Serializable {

  @ApiModelProperty("收件人")
  private String to;

  @ApiModelProperty("邮件标题")
  private String subject;

  @ApiModelProperty("邮件内容")
  private String content;
}
