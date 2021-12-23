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
@TableName("pp_merchant")
public class PpMerchant {

  @TableId(type = IdType.AUTO)
  public Long id;

  @ApiModelProperty(value = "名称")
  private String name;

  @ApiModelProperty(value = "第三方代付接口编码")
  private String ppInterfaceCode;

  @ApiModelProperty(value = "状态: [1 - 启用, 0 - 禁用]")
  private Integer status;

  @ApiModelProperty(value = "代付累计次数")
  private Long proxyTimes;

  @ApiModelProperty(value = "代付累计金额")
  private BigDecimal proxyAmount;

  @ApiModelProperty(value = "商户参数JSON")
  private String parameters;

  @ApiModelProperty(value = "商户限制信息JSON")
  private String merLimits;

  @ApiModelProperty(value = "排序")
  private Integer sort;

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
