package com.gameplat.admin.model.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 反馈消息VO
 *
 * @author lily
 */
@Data
public class MessageFeedbackVO implements Serializable {

  @Schema(description = "主键ID")
  private Long id;

  @Schema(description = "反馈标题")
  private String title;

  @Schema(description = "反馈内容")
  private String content;

  @Schema(description = "反馈意见图片")
  private String imgUrl;

  @Schema(description = "是否已读，0未读 1已读")
  private Integer isRead;

  @Schema(description = "站内信类型，0:活动建议 1:功能建议 2:游戏BUG 3:其他问题")
  private String letterType;

  @Schema(description = "订单id")
  private String orderId;

  @Schema(description = "站内信回复者名字")
  private String sendName;

  @Schema(description = "消息状态，1 有效，0 无效")
  private Integer status;

  @Schema(description = "信件类别(0:普通信件,1:活动信件,2:回复信件,3:系统消息)")
  private Integer type;

  @Schema(description = "发送用户名id")
  private Long userId;

  @Schema(description = "发送用户名")
  private String username;

  @Schema(description = "用户发送的内容")
  private String userContent;

  @Schema(description = "用户发送的图片")
  private String userImgUrl;

  @Schema(description = "创建人")
  @TableField(fill = FieldFill.INSERT)
  private String createBy;

  @Schema(description = "创建时间")
  @TableField(fill = FieldFill.INSERT)
  private Date createTime;

  @Schema(description = "更新人")
  @TableField(fill = FieldFill.INSERT_UPDATE)
  private String updateBy;

  @Schema(description = "更新时间")
  @TableField(fill = FieldFill.INSERT_UPDATE)
  private Date updateTime;
}
