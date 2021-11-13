package com.gameplat.admin.model.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
public class MemberAddDTO implements Serializable {

  @NotEmpty(message = "账号不能为空")
  @Length(min = 4, max = 20, message = "账号长度必须是4～20个字符")
  private String account;

  @NotEmpty(message = "昵称不能为空")
  @Length(min = 2, max = 50, message = "昵称长度必须是2～50个字符")
  private String nickname;

  @NotEmpty(message = "密码不能为空")
  @Length(min = 4, max = 20, message = "密码长度必须是4～20个字符")
  private String password;

  @Length(min = 2, max = 50, message = "真实姓名长度必须是2～50个字符")
  private String realName;

  @NotEmpty(message = "提现状态不能为空")
  private String withdrawFlag;

  @NotEmpty(message = "用户类型不能为空")
  private String userType;

  @NotEmpty(message = "语言不能为空")
  private String language;

  @NotEmpty(message = "用户层级不能为空")
  private String userLevel;

  @NotNull(message = "用户状态不能为空")
  private Integer status;

  @Range(min = 0, max = 1, message = "性别只能选择男或女")
  private String sex;

  @Length(max = 20, message = "QQ号码不能超过20个字符")
  private String qq;

  @Email(message = "电子邮箱格式不正确")
  @Length(max = 50, message = "电子邮箱不能超过50个字符")
  private String email;

  @Length(max = 10, message = "手机区号不能超过10个字符")
  private String telAreaCode;

  @Length(max = 20, message = "手机号码不能超过20个字符")
  private String phone;

  @Length(max = 20, message = "微信号码不能超过20个字符")
  private String wechat;

  private Date birthday;

  @Length(max = 255, message = "会员备注不能超过255个字符")
  private String remark;

  @NotEmpty(message = "返点等级不能为空")
  private String rebate;

  private String registerIp;

  private String registerHost;

  private String registerBrowser;

  private String registerOs;

  private String registerUserAgent;
}
