package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author lily
 * @date 2021/11/28
 */
@Data
@ApiModel(value="SysInformation对象", description="系统用户个人消息表")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class SysInformation implements Serializable {

    /**
     * 主键
     */
    @ApiModelProperty(value = "id")
    @TableId(type = IdType.AUTO)
    private Long id;

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
     * 创建者
     */
    @ApiModelProperty(value = "创建者")
    private String createBy;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 更新者
     */
    @ApiModelProperty(value = "更新者")
    private String updateBy;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    /**
     * 用户意见反馈记录id
     */
    @ApiModelProperty(value = "用户反馈记录id")
    private Long userFeedbackId;

    /**
     * 标题
     */
    @ApiModelProperty(value = "用户反馈意见标题")
    private String userFeedbackTitle;

    /**
     * 内容
     */
    @ApiModelProperty(value = "用户反馈意见内容")
    private String userFeedbackContent;

    /**
     * 内容
     */
    @ApiModelProperty(value = "用户反馈意见图片")
    private String userFeedbackImgUrl;
}
