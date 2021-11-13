package com.gameplat.admin.model.dto;

import lombok.Data;

/**
 * 用户信息DTO
 * @author three
 */
@Data
public class UserInfoDTO {

  /**
   * 账号
   */
  private String loginName;

  /**
   * 昵称
   */
  private String nickName;

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
  private String startDate;

  /**
   * 结束时间
   */
  private String endDate;
}
