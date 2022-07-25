package com.gameplat.admin.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author kb @Date 2022/2/24 17:13 @Version 1.0
 */
@Data
public class ValidWithdrawDto implements Serializable {

  /** 打码量id */
  private Long id;

  /** 常态打码量 */
  @NotNull(message = "常态打码量不能为空")
  private BigDecimal mormDml;

  /** 优惠打码量 */
  @NotNull(message = "优惠打码量不能为空")
  private BigDecimal discountDml;

  /** 备注 */
  private String remark;

  /** 会员账号 */
  private String username;

  /** 用户id */
  private Long userId;

}
