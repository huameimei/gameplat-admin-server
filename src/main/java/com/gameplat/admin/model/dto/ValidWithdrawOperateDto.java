package com.gameplat.admin.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author kb @Date 2022/2/24 17:13 @Version 1.0
 */
@Data
public class ValidWithdrawOperateDto implements Serializable {

  /** 常态打码量 */
  private BigDecimal mormDml;

  /** 备注 */
  private String remarks;

  /** 会员账号 */
  private String username;

  /** 用户id */
  private Long userId;

}
