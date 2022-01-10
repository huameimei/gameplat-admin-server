package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 会员流水 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("member_bill")
public class MemberBill {

  @ApiModelProperty(value = "主键")
  private Long id;

  @ApiModelProperty(value = "会员编号")
  private Long memberId;

  @ApiModelProperty(value = "会员账号")
  private String account;

  @ApiModelProperty(value = "代理路径")
  private String memberPath;

  @ApiModelProperty(value = "交易类型")
  private Integer tranType;

  @ApiModelProperty(value = "订单号")
  private String orderNo;

  @ApiModelProperty(value = "交易金额")
  private BigDecimal amount;

  @ApiModelProperty(value = "账变前的余额")
  private BigDecimal balance;

  @ApiModelProperty(value = "备注")
  private String remark;

  @ApiModelProperty(value = "账变内容")
  private String content;

  @ApiModelProperty(value = "操作人")
  private String operator;

  @ApiModelProperty(value = "分表ID")
  private Integer tableIndex;

  @ApiModelProperty(value = "添加时间")
  @TableField(fill = FieldFill.INSERT)
  @JsonFormat(pattern = "yyyy-MM-dd")
  private Date createTime;
}
