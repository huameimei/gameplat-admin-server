package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 活动类型
 *
 * @author kenvin
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("activity_type")
public class ActivityType implements Serializable {

  private static final long serialVersionUID = 1L;

  /** 编号 */
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  /** 活动类型 */
  private String typeCode;

  /** 活动类型名称 */
  private String typeName;

  /** 创建时间 */
  @TableField(fill = FieldFill.INSERT)
  private Date createTime;

  /** 更新时间 */
  @TableField(fill = FieldFill.INSERT_UPDATE)
  private Date updateTime;

  /** 创建人 */
  @TableField(fill = FieldFill.INSERT)
  private String createBy;

  /** 更新人 */
  @TableField(fill = FieldFill.INSERT_UPDATE)
  private String updateBy;

  /** 备注 */
  private String remark;

  /** 排序 */
  private Integer sort;

  /** 状态 */
  private Integer typeStatus;

  /** 浮窗状态 */
  private Integer floatStatus;

  /** 浮窗logo */
  private String floatLogo;

  /** 浮窗url */
  private String floatUrl;

  /** 语言 */
  private String language;
}
