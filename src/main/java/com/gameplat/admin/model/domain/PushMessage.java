package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;

@Data
@TableName("push_message")
public class PushMessage {

    @TableId(type = IdType.AUTO)
    public Long id;

    @ApiModelProperty(value = "推送消息的标题")
    public String messageTitle;

    @ApiModelProperty(value = "推送消息的内容")
    public String messageContent;

    @ApiModelProperty(value = "接收人会员ID")
    public Long userId;

    @ApiModelProperty(value = "接收人消息的账号")
    public String userAccount;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "发送人消息的账号")
    public String createBy;

    @ApiModelProperty(value = "是否已读: 0-未读; 1-已读")
    public Integer readStatus;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "添加时间")
    public Date createTime;

    @ApiModelProperty(value = "接收者阅读消息的时间")
    public Date readTime;

    @ApiModelProperty(value = "接收者删除信息的标志0未删除1已删除")
    public Integer acceptRemoveFlag;

    @ApiModelProperty(value = "发送者删除信息的标志0未删除1已删除")
    public Integer sendRemoveFlag;

    @ApiModelProperty(value = "是否即时消息,0:否,1:是")
    public Integer immediateFlag;

}
