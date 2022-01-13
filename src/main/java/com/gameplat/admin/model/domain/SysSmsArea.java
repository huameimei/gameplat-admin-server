package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("sys_sms_area")
@EqualsAndHashCode(callSuper = false)
public class SysSmsArea implements Serializable {

  @TableId(type = IdType.AUTO)
  private Long id;
  /**
   * 编码
   */
  private String code;
  /**
   * 国家/地区
   */
  private String name;

  /**
   * 创建者
   */
  @TableField(fill = FieldFill.INSERT)
  private String createBy;

  /**
   * 创建时间
   */
  @TableField(fill = FieldFill.INSERT)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createTime;

  /**
   * 更新人
   */
  @TableField(fill = FieldFill.INSERT_UPDATE)
  private String updateBy;

  /**
   * 更新时间
   */
  @TableField(fill = FieldFill.INSERT_UPDATE)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date updateTime;

  /**
   * 状态 0 禁用 1 启用
   */
  private Integer status;

}
