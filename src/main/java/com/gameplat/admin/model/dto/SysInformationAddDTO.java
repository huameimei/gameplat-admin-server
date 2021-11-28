package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lily
 * @description 新增系统用户个人消息入参
 * @date 2021/11/28
 */
@Data
public class SysInformationAddDTO implements Serializable {

    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String title;

    /**
     * 内容
     */
    @ApiModelProperty(value = "内容")
    private String content;

    /**
     * 内容
     */
    @ApiModelProperty(value = "图片")
    private String imgUrl;
    /**
     * 0未读 1已读
     */
    @ApiModelProperty(value = "是否已读")
    private Byte isRead;

    /**
     * 0:活动建议 1:功能建议 2:游戏BUG 3:其他问题
     */
    @ApiModelProperty(value = "站内信类型")
    private Byte letterType;

    /**
     * 到账相关的订单id
     */
    @ApiModelProperty(value = "订单id")
    private String orderId;

    /**
     * 站内信回复者名字
     */
    @ApiModelProperty(value = "站内信回复者名字")
    private String sendName;

    /**
     * 0未读 1已读
     */
    @ApiModelProperty(value = "状态")
    private Byte status;

    /**
     * 信件类别(0:普通信件,1:活动信件,2:回复信件,3:系统消息,4:用户反馈)
     */
    @ApiModelProperty(value = "信件类别")
    private Byte type;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Long userId;

    /**
     * 用户名字
     */
    @ApiModelProperty(value = "用户名称")
    private String userName;

    /**
     * 用户意见反馈记录id
     */
    @ApiModelProperty(value = "用户反馈记录id")
    private Long userFeedbackId;
}
