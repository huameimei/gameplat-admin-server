package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
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
  @ApiModelProperty(value = "账号")
  private String account;

  /** 昵称 */
  @ApiModelProperty(value = "昵称")
  private String nickName;

  /** 用户类型 */
  @ApiModelProperty(value = "用户类型")
  private String userType;

  /** 电话 */
  @ApiModelProperty(value = "电话")
  private String phone;

  /** 状态 */
  @ApiModelProperty(value = "状态")
  private Integer status;

  /** 开始时间 */
  @ApiModelProperty(value = "开始时间")
  private String beginTime;

  /** 结束时间 */
  @ApiModelProperty(value = "结束时间")
  private String endTime;

  /** 最近登录ip */
  @ApiModelProperty(value = "最近登录ip")
  private String loginIp;
}
