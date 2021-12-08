package com.gameplat.admin.model.dto;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class MemberBankAddDTO implements Serializable {

  @NotNull(message = "会员ID不能为空")
  private Long memberId;

  @NotEmpty(message = "会员账号不能为空")
  private String account;

  @NotEmpty(message = "银行名称/主网不能为空")
  @Length(max = 64, message = "银行名称/主网不能超过64个字符")
  private String bankName;

  @Length(max = 50, message = "真实姓名长度超过50个字符")
  private String realName;

  @NotEmpty(message = "卡号/钱包地址不能为空")
  @Length(max = 100, message = "卡号/钱包地址不能超过100个字符")
  private String cardNo;

  @Length(max = 100, message = "所属省份不能超过100个字符")
  private String province;

  @Length(max = 100, message = "所属市县不能超过100个字符")
  private String city;

  @Length(max = 100, message = "银行地址不能超过100个字符")
  private String subAddress;

  @NotEmpty(message = "是否默认不能为空")
  private String isDefault;

  @NotEmpty(message = "类型不能为空")
  private String type;

  @NotEmpty(message = "币种不能为空")
  private String currency;

  @Length(max = 255, message = "备注不能超过255个字符")
  private String remark;
}
