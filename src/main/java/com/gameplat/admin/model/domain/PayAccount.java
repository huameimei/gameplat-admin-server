package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("pay_account")
public class PayAccount {

  @TableId(type = IdType.AUTO)
  public Long id;

  private String name;

  private String account;

  private String owner;

  private String payType;

  private String bankName;

  private String bankAddress;

  private String qrCode;

  private String userLevels;

  private String remarks;

  private Integer sort;

  @ApiModelProperty(value = "状态: [0 - 启用, 1 - 禁用]")
  private Integer status;

  private Long rechargeTimes;

  private BigDecimal rechargeAmount;

  private String orderRemark;

  @ApiModelProperty(value = "0:启用，1：关闭")
  private Integer orderRemarkStatus;

  private String limitInfo;

  private String handleTip;

  private String emailAddress;

  /**
   * 创建者
   */
  @TableField(fill = FieldFill.INSERT)
  @ApiModelProperty(value = "创建者")
  private String createBy;

  /**
   * 创建时间
   */
  @TableField(fill = FieldFill.INSERT)
  @ApiModelProperty(value = "创建时间")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createTime;

  /**
   * 更新者
   */
  @TableField(fill = FieldFill.INSERT_UPDATE)
  @ApiModelProperty(value = "更新者")
  private String updateBy;

  /**
   * 更新时间
   */
  @TableField(fill = FieldFill.INSERT_UPDATE)
  @ApiModelProperty(value = "更新时间")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date updateTime;

}
