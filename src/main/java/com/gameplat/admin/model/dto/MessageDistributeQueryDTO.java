package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
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
  @ApiModelProperty(value = "消息id")
  private Long messageId;

  @ApiModelProperty(value = "推送范围")
  @NotNull(message = "推送范围不能为空")
  private Integer pushRange;

  @ApiModelProperty(value = "会员账号")
  private String userAccount;

  @ApiModelProperty(value = "充值层级/会员层级")
  private Integer rechargeLevel;

  @ApiModelProperty(value = "VIP等级")
  private Integer vipLevel;

  @ApiModelProperty(value = "读取状态")
  private Integer read;

  @ApiModelProperty(value = "关联会员账号")
  private String linkAccount;
}
