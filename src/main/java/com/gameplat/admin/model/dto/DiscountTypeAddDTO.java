package com.gameplat.admin.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class DiscountTypeAddDTO implements Serializable {

  private String name;

  private Integer rechargeFlag;

  private Integer value;

  private Integer dateType;

  private Integer sort;

  private Integer status;

  private Integer mode;

  private BigDecimal discountAmount;

  /** 创建者 */
  @Schema(description = "创建者")
  private String createBy;

  /** 创建时间 */
  @Schema(description = "创建时间")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createTime;

  /** 更新者 */
  @Schema(description = "更新者")
  private String updateBy;

  /** 更新时间 */
  @Schema(description = "更新时间")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date updateTime;
}
