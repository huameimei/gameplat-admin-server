package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 会员流水 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("member_bill")
public class MemberBill {

  private Long id;

  private Long memberId;

  private String account;

  private String memberPath;

  private Integer tranType;

  private String orderNo;

  private BigDecimal amount;

  private BigDecimal balance;

  private String remark;

  private String content;

  private String operator;

  private Integer tableIndex;

  @TableField(fill = FieldFill.INSERT)
  @JsonFormat(pattern = "yyyy-MM-dd")
  private Date createTime;
}
