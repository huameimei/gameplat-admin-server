package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

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

  private String memberLevels;

  private String remarks;

  private Integer sort;

  @ApiModelProperty(value = "状态: [1 - 启用, 0 - 禁用]")
  private Integer status;

  private Long rechargeTimes;

  private BigDecimal rechargeAmount;

  private String orderRemark;

  @ApiModelProperty(value = "1:启用，0：关闭")
  private Integer orderRemarkStatus;

  private String limitInfo;

  private String handleTip;

  private String emailAddress;

  private Integer typeSubscript;

  private String url;

  @ApiModelProperty(value = "1:普通账户，2：vpi账户")
  private Integer type;

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
