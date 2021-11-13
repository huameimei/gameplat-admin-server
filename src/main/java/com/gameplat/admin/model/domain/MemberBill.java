package com.gameplat.admin.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/** 会员流水 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberBill {

  private Long id;

  private String account;

  private String memberPaths;

  private Integer tranType;

  private String orderNo;

  private BigDecimal amount;

  private BigDecimal balance;

  private String remark;

  private String content;

  private String operator;

  private Integer tableIndex;

  private Date createTime;
}
