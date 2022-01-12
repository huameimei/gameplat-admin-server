package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 站内信
 *
 * @author: kenvin
 * @date: 2021/4/28 15:53
 * @desc:
 */
@Data
public class MessageInfoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "消息标题")
    private String messageTitle;

    @ApiModelProperty(value = "消息内容")
    private String messageContent;

    @ApiModelProperty(value = "PC端图片")
    private String pcImage;

    @ApiModelProperty(value = "APP端图片")
    private String appImage;

    @ApiModelProperty(value = "推送范围,1:部分会员,2:所有会有,3:在线会员,4:指定层级,5:代理线")
    private Integer pushRange;

    @ApiModelProperty(value = "是否弹窗: 0 否  1 是")
    private Integer popupsFlag;

    @ApiModelProperty(value = "弹出次数: 1 一次  2 多次")
    private Integer popupsFrequency;

    @ApiModelProperty(value = "消息类型,1:文本弹窗,2:图片弹窗")
    private Integer messageType;

    @ApiModelProperty(value = "弹窗类型")
    private String popupsType;

    @ApiModelProperty(value = "是否即时消息: 0 否  1 是")
    private Integer immediateFlag;

    @ApiModelProperty(value = "状态：0 过期,1 有效")
    private Integer status;

    @ApiModelProperty(value = "弹窗排序")
    private Integer sort;

    @ApiModelProperty(value = "关联账号")
    private String linkAccount;

    @ApiModelProperty(value = "会员层级")
    private String level;

    @ApiModelProperty(value = "语种")
    private String language;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "开始时间")
    private Date beginTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
