package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.SysRole;
import com.gameplat.admin.model.dto.AuthMenuDTO;
import com.gameplat.admin.model.dto.OperRoleDTO;
import com.gameplat.admin.model.dto.RoleDTO;
import com.gameplat.admin.model.vo.RoleVo;
import java.util.List;
import java.util.Set;

/**
 * 角色管理 业务层处理
 *
 * @author three
 */
public interface SysRoleService extends IService<SysRole> {

  IPage<RoleVo> selectGroupList(PageDTO<SysRole> page, RoleDTO dto);

  void insertGroup(OperRoleDTO roleDTO);

  void updateGroup(OperRoleDTO roleDTO);

  void deleteGroupById(Long id);

  IPage<RoleVo> selectRoleList(PageDTO<SysRole> page, RoleDTO dto);

  void insertRole(OperRoleDTO roleDTO);

  void updateRole(OperRoleDTO roleDTO);

  void deleteRoleByIds(String ids);

  boolean checkRoleNameUnique(Long id, String groupName);

  boolean checkRoleKeyUnique(Long id, String roleKey);

  List<Long> getRoleMenuList(Long roleId);

  /**
   * 分配菜单权限
   *
   * @param role AuthMenuDTO
   */
  void authMenuScope(AuthMenuDTO role);

  Set<String> getRolesByUserId(Long userId);

  void changeDefault(Long id, Integer defaultFlag);
}
