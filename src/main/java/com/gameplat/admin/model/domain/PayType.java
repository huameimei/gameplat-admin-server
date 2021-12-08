package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;

@Data
@TableName("pay_type")
public class PayType {

  @TableId(type = IdType.AUTO)
  public Long id;

  private String name;

  private String code;

  private String bankFlag;

  private Integer sort;

  private String rechargeDesc;

  @ApiModelProperty(value = "是否支持转账: [0 - 否, 1 - 是]")
  private Integer transferEnabled;

  @ApiModelProperty(value = "是否支持在线支付: [0 - 否, 1 - 是]")
  private Integer onlinePayEnabled;

  @ApiModelProperty(value = "状态: [0 - 启用, 1 - 禁用]")
  private Integer status;

  @ApiModelProperty(value = "是否系统支付编码: [0 - 否, 1 - 是]")
  private Integer isSysCode;

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
