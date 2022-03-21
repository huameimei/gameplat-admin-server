package com.gameplat.admin.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author kb @Date 2022/3/5 14:09 @Version 1.0
 */
@Data
public class AccountReportVo implements Serializable {

  /** 账户 */
  private String account;

  /** 余额 */
  private BigDecimal goodMoney;
}
