package com.gameplat.admin.model.dto;

import lombok.Data;

/**
 * 用户DTO
 * @author three
 */
@Data
public class UserDTO {

  private Integer id;

  /**
   * 账号
   */
  private String account;

  /**
   * 昵称
   */
  private String nickName;

  /**
   * 用户类型
   */
  private Integer userType;

  /**
   * 电话
   */
  private String phone;

  /**
   * 状态
   */
  private Integer status;

  /**
   * 开始时间
   */
  private String beginTime;

  /**
   * 结束时间
   */
  private String endTime;
}
