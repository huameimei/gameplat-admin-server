package com.gameplat.admin.controller.open.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.bean.router.VueRouter;
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
import com.gameplat.model.entity.sys.SysMenu;
import com.gameplat.model.entity.sys.SysRole;
import com.gameplat.security.SecurityUserHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "角色管理")
@Slf4j
@RestController
@RequestMapping("/api/admin/manager/role")
public class OpenRoleController {

  @Autowired private SysRoleService roleService;

  @Autowired private PermissionService permissionService;

  @Operation(summary = "查询")
  @GetMapping("/list")
  @PreAuthorize("hasAuthority('system:role:view')")
  public IPage<RoleVo> list(PageDTO<SysRole> page, RoleDTO dto) {
    return roleService.selectRoleList(page, dto);
  }

  @Operation(summary = "添加")
  @PostMapping("/add")
  @PreAuthorize("hasAuthority('system:role:add')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.ADMIN, desc = "'添加角色【'+#dto.roleName+'】'")
  public void save(@Validated(Groups.INSERT.class) @RequestBody OperRoleDTO dto) {
    roleService.insertRole(dto);
  }

  @Operation(summary = "编辑")
  @PostMapping("/edit")
  @PreAuthorize("hasAuthority('system:role:edit')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.ADMIN, desc = "'修改角色【'+#dto.roleName+'】'")
  public void update(@Validated(Groups.UPDATE.class) @RequestBody OperRoleDTO dto) {
    roleService.updateRole(dto);
  }

  @Operation(summary = "删除")
  @PostMapping("/remove/{id}")
  @PreAuthorize("hasAuthority('system:role:remove')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.ADMIN, desc = "'删除角色id='+#id")
  public void remove(@PathVariable Long id) {
    roleService.deleteGroupById(id);
  }

  @Operation(summary = "分配菜单/权限")
  @PostMapping("/authMenuScope")
  @PreAuthorize("hasAuthority('system:role:authMenu')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.ADMIN, desc = "'授权角色菜单角色Id='+#dto.roleId")
  public void authMenuScope(@Validated AuthMenuDTO dto) {
    roleService.authMenuScope(dto);
  }

  @Operation(summary = "根据角色ID获取菜单")
  @GetMapping("/roleMenuList/{id}")
  @PreAuthorize("hasAuthority('system:role:authMenu')")
  public List<Long> roleMenuList(@PathVariable Long id) {
    return roleService.getRoleMenuList(id);
  }

  @Operation(summary = "修改默认")
  @PostMapping("/changeDefault/{id}/{defaultFlag}")
  @PreAuthorize("hasAuthority('system:role:changeDefault')")
  public void changeDefault(@PathVariable Long id, Integer defaultFlag) {
    roleService.changeDefault(id, defaultFlag);
  }

  @Operation(summary = "获取菜单列表")
  @GetMapping("/menuList")
  public ArrayList<VueRouter<SysMenu>> menuList() {
    return permissionService.getMenuList(SecurityUserHolder.getUsername());
  }

  @Operation(summary = "检查角色名称是否唯一")
  @GetMapping("/checkRoleNameUnique/{id}/{roleName}")
  public boolean checkRoleNameUnique(@PathVariable Long id, @PathVariable String roleName) {
    return roleService.checkRoleNameUnique(id, roleName);
  }

  @Operation(summary = "检查角色KEY是否唯一")
  @GetMapping("/checkRoleKeyUnique/{id}/{roleKey}")
  public boolean checkRoleKeyUnique(@PathVariable Long id, @PathVariable String roleKey) {
    return roleService.checkRoleKeyUnique(id, roleKey);
  }
}
