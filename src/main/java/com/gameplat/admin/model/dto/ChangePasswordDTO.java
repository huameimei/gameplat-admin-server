package com.gameplat.admin.model.dto;

import lombok.Data;

/**
 * 修改密码DTO
 *
 * @author three
 */
@Data
public class ChangePasswordDTO {

  /** 旧密码 */
  private String oldPassWord;
  /** 新密码 */
  private String newPassWord;
}
