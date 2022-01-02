package com.gameplat.admin.model.dto;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class MemberBankEditDTO implements Serializable {

  @NotNull(message = "编号不能为空")
  private Long id;

  @NotEmpty(message = "会员账号不能为空")
  private String account;

  @NotEmpty(message = "银行名称不能为空")
  @Length(max = 64, message = "银行名称不能超过64个字符")
  private String bankName;

  @NotEmpty(message = "银行卡号不能为空")
  @Length(max = 100, message = "银行名称不能超过100个字符")
  private String cardNo;

  @Length(max = 100, message = "银行地址不能超过100个字符")
  private String address;

  @NotEmpty(message = "币种不能为空")
  private String currency;

  @Length(max = 255, message = "备注不能超过255个字符")
  private String remark;
}
