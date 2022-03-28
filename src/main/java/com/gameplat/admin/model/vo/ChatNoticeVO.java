package com.gameplat.admin.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameplat.admin.util.Date2LongSerializerUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lily
 * @description
 * @date 2022/2/9
 */
@Data
public class ChatNoticeVO implements Serializable {

  @ApiModelProperty(value = "主键")
  private Long id;

  @ApiModelProperty(value = "公告标题")
  private String noticeTitle;

  @ApiModelProperty(value = "排序")
  private Integer sort;

  @ApiModelProperty(value = "起始有效时间")
  private Long beginDate;

  @ApiModelProperty(value = "截止有效时间")
  private Long endDate;

  @ApiModelProperty(value = "公告内容")
  private String noticeContent;

  @ApiModelProperty(value = "公告类型")
  private Integer noticeType;

  @ApiModelProperty(value = "最近修改人")
  private String updateBy;

  @ApiModelProperty(value = "最近修改时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date updateTime;

  @ApiModelProperty(value = "状态")
  private Integer status;

  @ApiModelProperty(value = "添加时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date createTime;

  @ApiModelProperty(value = "添加人")
  private String createBy;
}
