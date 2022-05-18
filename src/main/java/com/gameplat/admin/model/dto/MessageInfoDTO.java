package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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

  @Schema(description = "主键")
  private Long id;

  @Schema(description = "消息标题")
  private String messageTitle;

  @Schema(description = "消息内容")
  private String messageContent;

  @Schema(description = "PC端图片")
  private String pcImage;

  @Schema(description = "APP端图片")
  private String appImage;

  @Schema(description = "推送范围,1:部分会员,2:所有会有,3:在线会员,4:指定层级,5:代理线")
  private Integer pushRange;

  @Schema(description = "是否弹窗: 0 否  1 是")
  private Integer popupsFlag;

  @Schema(description = "弹出次数: 1 一次  2 多次")
  private Integer popupsFrequency;

  @Schema(description = "消息类型,1:文本弹窗,2:图片弹窗")
  private Integer messageType;

  @Schema(description = "弹窗类型")
  private String popupsType;

  @Schema(description = "是否即时消息: 0 否  1 是")
  private Integer immediateFlag;

  @Schema(description = "状态：0 过期,1 有效")
  private Integer status;

  @Schema(description = "弹窗排序")
  private Integer sort;

  @Schema(description = "关联账号")
  private String linkAccount;

  @Schema(description = "会员层级")
  private String level;

  @Schema(description = "语种")
  private String language;

  @Schema(description = "备注")
  private String remarks;

  @Schema(description = "开始时间")
  private Date beginTime;

  @Schema(description = "结束时间")
  private Date endTime;

  @Schema(description = "创建人")
  private String createBy;

  @Schema(description = "创建时间")
  private Date createTime;

  @Schema(description = "更新人")
  private String updateBy;

  @Schema(description = "更新时间")
  private Date updateTime;
}
