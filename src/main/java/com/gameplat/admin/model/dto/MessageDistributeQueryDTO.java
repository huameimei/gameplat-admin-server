package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 消息分发到会员查询DTO
 *
 * @author: kenvin
 * @date: 2021/5/1 9:22
 * @desc:
 */
@Data
public class MessageDistributeQueryDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  @NotNull(message = "消息id不能为空")
  @Schema(description = "消息id")
  private Long messageId;

  @Schema(description = "推送范围")
  @NotNull(message = "推送范围不能为空")
  private Integer pushRange;

  @Schema(description = "会员账号")
  private String userAccount;

  @Schema(description = "充值层级/会员层级")
  private Integer rechargeLevel;

  @Schema(description = "VIP等级")
  private Integer vipLevel;

  @Schema(description = "读取状态")
  private Integer read;

  @Schema(description = "关联会员账号")
  private String linkAccount;
}
