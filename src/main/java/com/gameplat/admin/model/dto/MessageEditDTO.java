package com.gameplat.admin.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gameplat.base.common.util.DateUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 站内信编辑DTO
 *
 * @author: kenvin
 * @date: 2021/4/28 15:53
 * @desc:
 */
@Data
public class MessageEditDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "语种")
    private String language;

    @ApiModelProperty(value = "推送范围,1:全部会员,2:部分会员,3:在线会员,4:会员层级,5:VIP等级,6:代理线")
    private Integer pushRange;

    @ApiModelProperty(value = "推送目标,用于接收不同推送范围填写的值")
    private String pushTarget;

    @ApiModelProperty(value = "消息类型,1:文本弹窗,2:图片弹窗")
    private Integer messageType;

    @ApiModelProperty(value = "弹窗类型")
    private String popupsType;

    @ApiModelProperty(value = "弹出次数: 1 一次, 2 多次")
    private Integer popupsFrequency;

    @ApiModelProperty(value = "弹窗排序")
    private Integer sort;

    @JsonFormat(pattern = DateUtil.YYYY_MM_DD_HH_MM_SS)
    @ApiModelProperty(value = "开始时间")
    private Date beginTime;

    @JsonFormat(pattern = DateUtil.YYYY_MM_DD_HH_MM_SS)
    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "消息标题")
    private String messageTitle;

    @ApiModelProperty(value = "消息内容")
    private String messageContent;

    @ApiModelProperty(value = "PC端图片")
    private String pcImage;

    @ApiModelProperty(value = "APP端图片")
    private String appImage;

    @ApiModelProperty(value = "是否即时消息: 0 否  1 是")
    private Integer immediateFlag;

}
