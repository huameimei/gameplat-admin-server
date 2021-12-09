package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 字典类型表 sys_dict_type
 *
 * @author three
 */
@Data
@TableName("sys_dict_type")
public class SysDictType implements Serializable {

  /** 字典主键 */
  @TableId private Long dictId;

  /** 字典名称 */
  private String dictName;

  /** 字典类型 */
  private String dictType;

  /** 状态（1正常 0停用） */
  private String status;

  /** 排序 */
  private Integer orderNum;

  private String remark;

  @TableField(fill = FieldFill.INSERT)
  private String createBy;

  @TableField(fill = FieldFill.INSERT)
  private Date createTime;

  @TableField(fill = FieldFill.UPDATE)
  private String updateBy;

  @TableField(fill = FieldFill.UPDATE)
  private Date updateTime;
}
