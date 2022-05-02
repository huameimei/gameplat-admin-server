package com.gameplat.admin.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class DiscountTypeVO implements Serializable {

  private Long id;

  private String name;

  private Integer dataType;

  private Integer rechargeFlag;

  private Integer value;

  private Integer sort;

  private Integer mode;

  private BigDecimal discountAmount;

  @ApiModelProperty(value = "状态: [1 - 启用, 0 - 禁用]")
  private Integer status;

  /** 创建者 */
  @ApiModelProperty(value = "创建者")
  private String createBy;

  /** 创建时间 */
  @ApiModelProperty(value = "创建时间")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createTime;

  /** 更新者 */
  @ApiModelProperty(value = "更新者")
  private String updateBy;

  /** 更新时间 */
  @ApiModelProperty(value = "更新时间")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date updateTime;
}
