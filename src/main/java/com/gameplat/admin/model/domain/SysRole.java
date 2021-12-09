package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * 角色表 sys_role
 *
 * @author three
 */
@Data
public class SysRole {

  private static final long serialVersionUID = 1L;

  /** 角色ID */
  @TableId(type = IdType.AUTO)
  private Long roleId;

  /** 角色名称 */
  private String roleName;

  /** 角色权限 */
  private String roleKey;

  /** 角色排序 */
  private Integer roleSort;

  /** 数据范围（1：所有数据权限；2：自定义数据权限；3：本部门数据权限；4：本部门及以下数据权限） */
  private String dataScope;

  /** 是否系统默认 */
  private Integer defaultFlag;

  private String remark;

  /** 角色状态（0正常 1停用） */
  private Integer status;

  /** 创建时间 */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @TableField(fill = FieldFill.INSERT)
  private Date createTime;

  /** 创建者 */
  @TableField(fill = FieldFill.INSERT)
  private String createBy;

  /** 更新者 */
  @TableField(fill = FieldFill.UPDATE)
  private String updateBy;

  /** 更新时间 */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @TableField(fill = FieldFill.UPDATE)
  private Date updateTime;

  @TableField(exist = false)
  private List<Long> menuIds;

  /** 菜单组 */
  @TableField(exist = false)
  private List<SysRoleMenu> roleMenusList;
}
