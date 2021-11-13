package com.gameplat.admin.model.dto;

import lombok.Data;


/**
 * 角色信息DTO
 * @author three
 */
@Data
public class RoleDTO {

  private Integer id;
  /**
   * 角色名称
   */
  private String roleName;
  /**
   * 角色权限
   */
  private String roleKey;
  /**
   * 角色状态（1正常 0停用）
   */
  private Integer status;
}
