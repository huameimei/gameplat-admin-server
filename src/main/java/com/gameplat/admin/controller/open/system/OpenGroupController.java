package com.gameplat.admin.controller.open.system;

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
import com.gameplat.common.security.SecurityUserHolder;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 分组管理控制类
 *
 * @author three
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/system/group")
public class OpenGroupController {

  @Autowired private SysRoleService roleService;

  @Autowired private PermissionService permissionService;

  @GetMapping("/page")
  @PreAuthorize("hasAuthority('system:grouping:view')")
  public IPage<RoleVo> page(PageDTO<SysRole> page, RoleDTO dto) {
    return roleService.selectGroupList(page, dto);
  }

  @PostMapping("/add")
  @PreAuthorize("hasAuthority('system:grouping:add')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.ADMIN, desc = "'添加分组['+#dto.roleName+']'")
  public void save(@Validated(Groups.INSERT.class) @RequestBody OperRoleDTO dto) {
    roleService.insertGroup(dto);
  }

  @PutMapping("/edit")
  @PreAuthorize("hasAuthority('system:grouping:edit')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.ADMIN, desc = "'修改分组['+#dto.roleName+']'")
  public void update(@Validated(Groups.UPDATE.class) @RequestBody OperRoleDTO dto) {
    roleService.updateGroup(dto);
  }

  @DeleteMapping("/delete/{id}")
  @PreAuthorize("hasAuthority('system:grouping:remove')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.ADMIN, desc = "'删除分组ids='+#ids")
  public void remove(@PathVariable Long id) {
    roleService.deleteGroupById(id);
  }

  @PostMapping("/authMenuScope")
  @PreAuthorize("hasAuthority('system:grouping:authMenu')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.ADMIN, desc = "'授权分组菜单角色Id'+#dto.roleId")
  public void authMenuScope(@Validated @RequestBody AuthMenuDTO dto) {
    roleService.authMenuScope(dto);
  }

  @GetMapping("/menuList")
  public ArrayList<VueRouter<SysMenu>> menuList() {
    return permissionService.getMenuList(SecurityUserHolder.getUsername());
  }

  @GetMapping("/groupMenuList/{id}")
  @PreAuthorize("hasAuthority('system:grouping:authMenu')")
  public List<Long> groupMenuList(@PathVariable Long id) {
    return roleService.getRoleMenuList(id);
  }

  @GetMapping("/checkGroupNameUnique/{id}/{groupName}")
  public boolean checkGroupNameUnique(@PathVariable Long id, @PathVariable String groupName) {
    return roleService.checkRoleNameUnique(id, groupName);
  }

  @GetMapping("/checkGroupKeyUnique/{id}/{groupKey}")
  public boolean checkGroupKeyUnique(@PathVariable Long id, @PathVariable String groupKey) {
    return roleService.checkRoleKeyUnique(id, groupKey);
  }
}
