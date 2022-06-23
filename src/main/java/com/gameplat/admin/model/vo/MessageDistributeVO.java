package com.gameplat.admin.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameplat.common.util.Date2LongSerializerUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 消息分发到会员列表
 *
 * @author: kenvin
 * @date: 2021/5/1 9:22
 * @desc:
 */
@Data
public class MessageDistributeVO implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "主键")
  private Long id;

  @Schema(description = "消息id")
  private Long messageId;

  @Schema(description = "用户id")
  private Long userId;

  @Schema(description = "用户账号")
  private String userAccount;

  @Schema(description = "充值层级")
  private Integer rechargeLevel;

  @Schema(description = "VIP等级")
  private Integer vipLevel;

  @Schema(description = "代理层级")
  private String agentLevel;

  @Schema(description = "阅读状态：0 未读  1 已读")
  private Integer readStatus;

  @Schema(description = "发送移除标识")
  private Integer sendRemoveFlag;

  @Schema(description = "接收移除标识")
  private Integer acceptRemoveFlag;

  @Schema(description = "创建人")
  private String createBy;

  @JsonSerialize(using = Date2LongSerializerUtils.class)
  @Schema(description = "创建时间")
  private Date createTime;

  @Schema(description = "更新人")
  private String updateBy;

  @JsonSerialize(using = Date2LongSerializerUtils.class)
  @Schema(description = "更新时间")
  private Date updateTime;

  @Schema(description = "在线状态")
  private Boolean online;
}
