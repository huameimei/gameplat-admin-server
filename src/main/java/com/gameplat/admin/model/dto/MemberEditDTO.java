package com.gameplat.admin.model.dto;

import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

/**
 * 编辑会员信息类
 *
 * @author three
 */
@Data
public class MemberEditDTO implements Serializable {

  @NotNull(message = "会员编号不能为空")
  private Long id;

  @Length(min = 2, max = 50, message = "昵称长度必须是2～50个字符")
  private String nickname;

  private Integer status;

  private Integer userLevel;

  private String withdrawFlag;

  private String language;

  private String rebate;

  @Length(min = 2, max = 50, message = "真实姓名长度必须是2～50个字符")
  private String realName;

  @Range(min = 0, max = 1, message = "性别只能选择男或女")
  private Integer sex;

  @Length(max = 20, message = "QQ号码不能超过20个字符")
  private String qq;

  @Length(max = 10, message = "手机区号不能超过10个字符")
  private String dialCode;

  @Length(max = 16, message = "手机号码不能超过16个字符")
  private String phone;

  @Length(max = 20, message = "微信号码不能超过20个字符")
  private String wechat;

  @Email(message = "电子邮箱格式不正确")
  @Length(max = 50, message = "电子邮箱不能超过50个字符")
  private String email;

  private Date birthday;

  @Length(max = 255, message = "会员备注不能超过255个字符")
  private String remark;
}
