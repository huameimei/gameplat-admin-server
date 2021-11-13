package com.gameplat.admin.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class OperUserDTO implements Serializable {

  private Long id;

  private String account;

  private String nickName;

  private String userType;

  private String avatar;

  private String phone;

  private String password;

  private String salt;

  private Integer status;

  private String safeCode;

  private Integer changFlag;

  private String setting;

  private String limitInfo;

  private Long roleId;
}
