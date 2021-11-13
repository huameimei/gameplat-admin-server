package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 快捷回复
 * @author three
 */
@Data
@TableName("quick_reply_config")
public class QuickReply {

    @TableId(type = IdType.AUTO)
    private Long quickId;
    private String message;
    private String messageType;
    private Integer defaultFlag;
    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;
}
