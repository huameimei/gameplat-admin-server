package com.gameplat.admin.model.domain;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
  @Excel(name = "代理账号", height = 20, width = 30, isImportField = "true_st")
  private String agentAccount;
  /** 推广码 */
  @Excel(name = "推广码", height = 20, width = 30, isImportField = "true_st")
  private String code;
  /** 推广地址 */
  @Excel(name = "推广地址", height = 20, width = 30, isImportField = "true_st")
  private String externalUrl;
  /** 推广类型 */
  @Excel(name = "推广页面", height = 20, width = 30, isImportField = "true_st")
  private Integer spreadType;
  /** 是否专属类型 */
  private Integer exclusiveFlag;
  /** 推广用户类型 */
  @Excel(name = "会员类型", replace = {"会员_0", "代理_1"}, height = 20, width = 30, isImportField = "true_st")
  private Integer userType;
  /** 用户层级 */
  @Excel(name = "会员层级", height = 20, width = 30, isImportField = "true_st")
  private Integer userLevel;
  /** 有效天数 */
  @Excel(name = "有效天数", height = 20, width = 30, isImportField = "true_st")
  private Integer effectiveDays;
  /** 访问数 */
  @Excel(name = "访问统计", height = 20, width = 30, isImportField = "true_st")
  private Integer visitCount;
  /** 注册数 */
  @Excel(name = "注册统计", height = 20, width = 30, isImportField = "true_st")
  private Integer registCount;
  /** 彩票反水比率 */
  private double rebate;
  /** 注册送彩金 */
  @Excel(name = "注册彩金", height = 20, width = 30, isImportField = "true_st")
  private BigDecimal discountAmount;
  /** 状态 */
  @Excel(name = "状态", replace = {"启用_1", "未启用_0"}, height = 20, width = 30, isImportField = "true_st")
  private Integer status;

  @TableField(fill = FieldFill.INSERT)
  @ApiModelProperty(value = "创建者")
  private String createBy;

  /** 创建时间 */
  @TableField(fill = FieldFill.INSERT)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @Excel(name = "创建时间", height = 20, width = 30, isImportField = "true_st")
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
