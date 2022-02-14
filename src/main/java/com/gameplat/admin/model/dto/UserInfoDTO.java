package com.gameplat.admin.model.dto;

import lombok.Data;

/**
 * 用户信息DTO
 *
 * @author three
 */
@Data
public class UserInfoDTO {

  /** 昵称 */
  private String nickname;

  /** 电话 */
  private String phone;

  private UserSettingDTO settings;
}
