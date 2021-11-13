package com.gameplat.admin.model.domain;

import lombok.Data;

/**
 * 角色和菜单关联 sys_role_menu
 * @author three
 */
@Data
public class SysRoleMenu {

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 菜单ID
     */
    private Long menuId;

}
