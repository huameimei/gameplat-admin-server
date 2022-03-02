package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lily
 * @description
 * @date 2022/2/9
 */
@Data
public class ChatNoticeQueryDTO implements Serializable {

  @ApiModelProperty(value = "公告标题")
  private String noticeTitle;

  @ApiModelProperty(value = "状态")
  private Integer status;
}
