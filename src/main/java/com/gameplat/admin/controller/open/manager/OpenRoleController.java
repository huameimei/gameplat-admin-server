package com.gameplat.admin.controller.open.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.bean.router.VueRouter;
import com.gameplat.admin.model.domain.SysMenu;
import com.gameplat.admin.model.domain.SysRole;
import com.gameplat.admin.model.dto.AuthMenuDTO;
import com.gameplat.admin.model.dto.OperRoleDTO;
import com.gameplat.admin.model.dto.RoleDTO;
import com.gameplat.admin.model.vo.RoleVo;
import com.gameplat.admin.service.PermissionService;
import com.gameplat.admin.service.SysRoleService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.common.group.Groups;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.security.SecurityUserHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色管理
 *
 * @author three
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/manager/role")
public class OpenRoleController {

  @Autowired private SysRoleService roleService;

  @Autowired private PermissionService permissionService;

  @GetMapping("/list")
  @PreAuthorize("hasAuthority('system:role:view')")
  public IPage<RoleVo> list(PageDTO<SysRole> page, RoleDTO dto) {
    return roleService.selectRoleList(page, dto);
  }

  @PostMapping("/add")
  @PreAuthorize("hasAuthority('system:role:add')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.ADMIN, desc = "'添加角色【'+#dto.roleName+'】'")
  public void save(@Validated(Groups.INSERT.class) @RequestBody OperRoleDTO dto) {
    roleService.insertRole(dto);
  }

  @PutMapping("/edit")
  @PreAuthorize("hasAuthority('system:role:edit')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.ADMIN, desc = "'修改角色【'+#dto.roleName+'】'")
  public void update(@Validated(Groups.UPDATE.class) @RequestBody OperRoleDTO dto) {
    roleService.updateRole(dto);
  }

  @DeleteMapping("/remove/{id}")
  @PreAuthorize("hasAuthority('system:role:remove')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.ADMIN, desc = "'删除角色id='+#id")
  public void remove(@PathVariable Long id) {
    roleService.deleteGroupById(id);
  }

  @PostMapping("/authMenuScope")
  @PreAuthorize("hasAuthority('system:role:authMenu')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.ADMIN, desc = "'授权角色菜单角色Id='+#dto.roleId")
  public void authMenuScope(@Validated @RequestBody AuthMenuDTO dto) {
    roleService.authMenuScope(dto);
  }

  @GetMapping("/roleMenuList/{id}")
  @PreAuthorize("hasAuthority('system:role:authMenu')")
  public List<Long> roleMenuList(@PathVariable Long id) {
    return roleService.getRoleMenuList(id);
  }

  @PutMapping("/changeDefault/{id}/{defaultFlag}")
  @PreAuthorize("hasAuthority('system:role:changeDefault')")
  public void changeDefault(@PathVariable Long id, Integer defaultFlag) {
    roleService.changeDefault(id, defaultFlag);
  }

  @GetMapping("/menuList")
  public ArrayList<VueRouter<SysMenu>> menuList() {
    return permissionService.getMenuList(SecurityUserHolder.getUsername());
  }

  @GetMapping("/checkRoleNameUnique/{id}/{roleName}")
  public boolean checkRoleNameUnique(@PathVariable Long id, @PathVariable String roleName) {
    return roleService.checkRoleNameUnique(id, roleName);
  }

  @GetMapping("/checkRoleKeyUnique/{id}/{roleKey}")
  public boolean checkRoleKeyUnique(@PathVariable Long id, @PathVariable String roleKey) {
    return roleService.checkRoleKeyUnique(id, roleKey);
  }
}
