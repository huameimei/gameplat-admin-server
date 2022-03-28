package com.gameplat.admin.model.dto;

import com.gameplat.common.group.Groups;
import com.gameplat.common.model.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 操作角色信息DTO
 *
 * @author three
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OperRoleDTO extends BaseDTO {

  @NotNull(groups = Groups.UPDATE.class, message = "角色编号不能为空")
  private Long id;

  /** 角色名称 */
  @NotNull(
      groups = {Groups.INSERT.class, Groups.UPDATE.class},
      message = "角色编号不能为空")
  private String roleName;

  /** 角色权限 */
  @NotNull(
      groups = {Groups.INSERT.class, Groups.UPDATE.class},
      message = "角色编码不能为空")
  private String roleKey;

  /** 角色排序 */
  private Integer roleSort;

  /** 是否系统默认 */
  private Integer defaultFlag;

  /** 角色状态（0正常 1停用） */
  private Integer status;

  private String remark;
}
