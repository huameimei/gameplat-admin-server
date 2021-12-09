package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 业务黑名单
 *
 * <p>针对会员帐号、会员层级配置的黑名单策略
 */
@Data
@TableName("biz_blacklist")
public class BizBlacklist implements Serializable {

  /** ID */
  @TableId(type = IdType.AUTO)
  private Long id;
  /**
   * 目标类型
   *
   * <ul>
   *   <li>0: 会员
   *   <li>1: 层级
   * </ul>
   */
  private Integer targetType;
  /**
   * 目标
   *
   * <p>会员帐号、层级值
   */
  private String target;
  /** 黑名单类型 */
  private String types;
  /** 备注 */
  private String remark;
  /**
   * 状态
   *
   * <ul>
   *   <li>0: 禁用
   *   <li>1: 启用
   * </ul>
   */
  private Integer status;

  @TableField(fill = FieldFill.INSERT)
  private String createBy;

  @TableField(fill = FieldFill.INSERT)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createTime;

  @TableField(fill = FieldFill.INSERT_UPDATE)
  private String updateBy;

  @TableField(fill = FieldFill.INSERT_UPDATE)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date updateTime;
}
