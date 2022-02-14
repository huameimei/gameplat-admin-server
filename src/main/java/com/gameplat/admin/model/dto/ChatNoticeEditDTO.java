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
public class ChatNoticeEditDTO implements Serializable {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "公告标题")
    private String noticeTitle;

    @ApiModelProperty(value = "公告内容")
    private String noticeContent;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "起始有效时间")
    private Long beginDate;

    @ApiModelProperty(value = "截止有效时间")
    private Long endDate;

    @ApiModelProperty(value = "状态")
    private Integer status;
}
