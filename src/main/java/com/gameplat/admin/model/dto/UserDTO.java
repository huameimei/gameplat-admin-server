package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户DTO
 *
 * @author three
 */
@Data
public class UserDTO {

  private Integer id;

  /** 账号 */
  @Schema(description = "账号")
  private String account;

  /** 昵称 */
  @Schema(description = "昵称")
  private String nickName;

  /** 用户类型 */
  @Schema(description = "用户类型")
  private String userType;

  /** 电话 */
  @Schema(description = "电话")
  private String phone;

  /** 状态 */
  @Schema(description = "状态")
  private Integer status;

  /** 开始时间 */
  @Schema(description = "开始时间")
  private String beginTime;

  /** 结束时间 */
  @Schema(description = "结束时间")
  private String endTime;

  /** 最近登录ip */
  @Schema(description = "最近登录ip")
  private String loginIp;

  /** 角色Id */
  @Schema(description = "角色Id")
  private Long roleId;
}
