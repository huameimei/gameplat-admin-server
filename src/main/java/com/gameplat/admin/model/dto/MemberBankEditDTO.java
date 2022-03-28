package com.gameplat.admin.model.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class MemberBankEditDTO implements Serializable {

  @NotNull(message = "编号不能为空")
  private Long id;

  @NotEmpty(message = "会员账号不能为空")
  private String account;

  @Length(max = 64, message = "银行名称不能超过{max}个字符")
  private String bankName;

  @NotEmpty(message = "银行卡号/虚拟币地址不能为空")
  @Length(max = 100, message = "银行卡号/虚拟币地址不能超过{max}个字符")
  private String cardNo;

  @Length(max = 100, message = "银行地址不能超过{max}个字符")
  private String address;

  @Length(max = 50, message = "币种不能超过{max}个字符")
  private String currency;

  @Length(max = 255, message = "备注不能超过{max}个字符")
  private String remark;

  @Length(max = 50, message = "持卡人长度超过{max}个字符")
  private String cardHolder;

  @Length(max = 50, message = "地址别名不能超过{max}个字符")
  private String alias;

  @Length(max = 64, message = "主网名称不能超过{max}个字符")
  private String network;
}
