package com.gameplat.admin.model.dto;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
public class MemberBankAddDTO implements Serializable {

  @NotNull(message = "会员ID不能为空")
  private Long memberId;

  @NotEmpty(message = "会员账号不能为空")
  private String account;

  @NotEmpty(message = "卡号/虚拟币地址不能为空")
  @Length(max = 100, message = "卡号/虚拟币地址不能超过{max}个字符")
  private String cardNo;

  @NotEmpty(message = "是否默认不能为空")
  private String isDefault;

  @NotEmpty(message = "类型不能为空")
  private String type;

  @Length(max = 255, message = "备注不能超过{max}个字符")
  private String remark;

  @Length(max = 64, message = "银行名称不能超过{max}个字符")
  private String bankName;

  @Length(max = 50, message = "持卡人长度超过{max}个字符")
  private String cardHolder;

  @Length(max = 100, message = "银行地址不能超过{max}个字符")
  private String address;

  @Length(max = 50, message = "币种长度不能超过{max}个字符")
  private String currency;

  @Length(max = 50, message = "地址别名不能超过{max}个字符")
  private String alias;

  @Length(max = 64, message = "主网名称不能超过{max}个字符")
  private String network;
}
