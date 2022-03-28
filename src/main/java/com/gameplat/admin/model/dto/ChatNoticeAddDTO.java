package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author lily
 * @description
 * @date 2022/2/9
 */
@Data
public class ChatNoticeAddDTO implements Serializable {

  @ApiModelProperty(value = "公告标题")
  @NotNull(message = "公告标题不能为空")
  private String noticeTitle;

  @ApiModelProperty(value = "公告内容")
  @NotNull(message = "公告内容不能为空")
  private String noticeContent;

  @ApiModelProperty(value = "排序")
  private Integer sort;

  @ApiModelProperty(value = "起始有效时间")
  private Long beginDate;

  @ApiModelProperty(value = "截止有效时间")
  private Long endDate;
}
