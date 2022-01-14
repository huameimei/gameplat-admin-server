package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 反馈消息
 *
 * @author kenvin
 */
@Data
@TableName("message_feedback")
public class MessageFeedback implements Serializable {

  /** 主键ID */
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  /** 反馈标题 */
  private String title;

  /** 反馈内容 */
  private String content;

  /** 反馈意见图片 */
  private String imgUrl;

  /** 是否已读，0未读 1已读 */
  private Integer isRead;

  /** 站内信类型，0:活动建议 1:功能建议 2:游戏BUG 3:其他问题 */
  private String letterType;

  /** 订单id */
  private String orderId;

  /** 站内信回复者名字 */
  private String sendName;

  /** 消息状态，1 有效，0 无效 */
  private Integer status;

  /** 信件类别(0:普通信件,1:活动信件,2:回复信件,3:系统消息) */
  private Integer type;

  /** 用户id */
  private Long userId;

  /** 用户名 */
  private String username;

  /** 创建人 */
  @TableField(fill = FieldFill.INSERT)
  private String createBy;

  /** 创建时间 */
  @TableField(fill = FieldFill.INSERT)
  private Date createTime;

  /** 更新人 */
  @TableField(fill = FieldFill.INSERT_UPDATE)
  private String updateBy;

  /** 更新时间 */
  @TableField(fill = FieldFill.INSERT_UPDATE)
  private Date updateTime;
}
