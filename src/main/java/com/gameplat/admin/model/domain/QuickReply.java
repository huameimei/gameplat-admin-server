package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * 快捷回复
 *
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

  @TableField(fill = FieldFill.INSERT)
  private String createBy;

  @TableField(fill = FieldFill.INSERT)
  private Date createTime;

  @TableField(fill = FieldFill.UPDATE)
  private String updateBy;

  @TableField(fill = FieldFill.UPDATE)
  private Date updateTime;
}
