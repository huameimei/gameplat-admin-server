package com.gameplat.admin.model.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 反馈消息VO
 *
 * @author lily
 */
@Data
@ApiModel("反馈消息VO")
public class MessageFeedbackVO implements Serializable {

  @ApiModelProperty("主键ID")
  private Long id;

  @ApiModelProperty("反馈标题")
  private String title;

  @ApiModelProperty("反馈内容")
  private String content;

  @ApiModelProperty("反馈意见图片")
  private String imgUrl;

  @ApiModelProperty("是否已读，0未读 1已读")
  private Integer isRead;

  @ApiModelProperty("站内信类型，0:活动建议 1:功能建议 2:游戏BUG 3:其他问题")
  private String letterType;

  @ApiModelProperty("订单id")
  private String orderId;

  @ApiModelProperty("站内信回复者名字")
  private String sendName;

  @ApiModelProperty("消息状态，1 有效，0 无效")
  private Integer status;

  @ApiModelProperty("信件类别(0:普通信件,1:活动信件,2:回复信件,3:系统消息)")
  private Integer type;

  @ApiModelProperty("发送用户名id")
  private Long userId;

  @ApiModelProperty("发送用户名")
  private String username;

  @ApiModelProperty("用户发送的内容")
  private String userContent;

  @ApiModelProperty("用户发送的图片")
  private String userImgUrl;

  @ApiModelProperty("创建人")
  @TableField(fill = FieldFill.INSERT)
  private String createBy;

  @ApiModelProperty("创建时间")
  @TableField(fill = FieldFill.INSERT)
  private Date createTime;

  @ApiModelProperty("更新人")
  @TableField(fill = FieldFill.INSERT_UPDATE)
  private String updateBy;

  @ApiModelProperty("更新时间")
  @TableField(fill = FieldFill.INSERT_UPDATE)
  private Date updateTime;


}
