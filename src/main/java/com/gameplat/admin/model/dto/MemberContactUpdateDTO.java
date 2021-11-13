package com.gameplat.admin.model.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class MemberContactUpdateDTO implements Serializable {

  @NotNull(message = "会员编号不能为空")
  private Long id;

  @Length(max = 20, message = "QQ号码不能超过20个字符")
  private String qq;

  @Email(message = "电子邮箱格式不正确")
  @Length(max = 50, message = "电子邮箱不能超过50个字符")
  private String email;

  @Length(max = 10, message = "手机区号不能超过10个字符")
  private String dialCode;

  @Length(max = 20, message = "手机号码不能超过20个字符")
  private String phone;

  @Length(max = 20, message = "微信号码不能超过20个字符")
  private String wechat;
}
