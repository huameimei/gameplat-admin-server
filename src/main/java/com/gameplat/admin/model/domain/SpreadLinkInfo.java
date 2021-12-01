package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 推广域名配置
 *
 * @author three
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("spread_link_info")
public class SpreadLinkInfo {

  @TableId(type = IdType.AUTO)
  private Long id;
  /** 代理Id */
  private Long agentId;
  /** 代理账号 */
  private String agentAccount;
  /** 推广码 */
  private String code;
  /** 推广地址 */
  private String externalUrl;
  /** 推广类型 */
  private Integer spreadType;
  /** 是否专属类型 */
  private Integer exclusiveFlag;
  /** 推广用户类型 */
  private Integer userType;
  /** 用户层级 */
  private Integer userLevel;
  /** 有效天数 */
  private Integer effectiveDays;
  /** 访问数 */
  private Integer visitCount;
  /** 注册数 */
  private Integer registCount;
  /** 彩票反水比率 */
  private double rebate;
  /** 注册送彩金 */
  private BigDecimal discountAmount;
  /** 状态 */
  private Integer status;

  @TableField(fill = FieldFill.INSERT)
  @ApiModelProperty(value = "创建者")
  private String createBy;

  /** 创建时间 */
  @TableField(fill = FieldFill.INSERT)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createTime;

  /** 更新者 */
  @TableField(fill = FieldFill.INSERT_UPDATE)
  private String updateBy;

  /** 更新时间 */
  @TableField(fill = FieldFill.INSERT_UPDATE)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date updateTime;

  private String remark;
}
