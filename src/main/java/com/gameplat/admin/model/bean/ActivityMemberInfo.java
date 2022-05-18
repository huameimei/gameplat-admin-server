package com.gameplat.admin.model.bean;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author aBen
 * @date 2021/5/6 17:02
 * @desc
 */
@Data
public class ActivityMemberInfo implements Serializable {

  @Schema(description = "用户Id")
  private Long userId;

  @Schema(description = "用户账号")
  private String username;

  @Schema(description = "0非正常  1正常")
  private Integer status;

  @Schema(description = "用户充值层级")
  private String userRank;

  @Schema(description = "用户VIP等级")
  private Integer userLevel;

  @Schema(description = "登录IP")
  private String lastLoginIp;
}
