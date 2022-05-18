package com.gameplat.admin.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameplat.admin.util.Date2LongSerializerUtils;
import io.swagger.v3.oas.annotations.media.Schema;
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

  @Schema(description = "主键")
  private Long id;

  @Schema(description = "公告标题")
  private String noticeTitle;

  @Schema(description = "排序")
  private Integer sort;

  @Schema(description = "起始有效时间")
  private Long beginDate;

  @Schema(description = "截止有效时间")
  private Long endDate;

  @Schema(description = "公告内容")
  private String noticeContent;

  @Schema(description = "公告类型")
  private Integer noticeType;

  @Schema(description = "最近修改人")
  private String updateBy;

  @Schema(description = "最近修改时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date updateTime;

  @Schema(description = "状态")
  private Integer status;

  @Schema(description = "添加时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date createTime;

  @Schema(description = "添加人")
  private String createBy;
}
