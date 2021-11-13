package com.gameplat.admin.service;

import com.gameplat.admin.model.domain.SysMenu;
import com.gameplat.admin.model.dto.MenuDTO;
import com.gameplat.admin.model.dto.OperMenuDTO;

import java.util.List;
import java.util.Set;

/**
 * 系统菜单 业务层处理
 *
 * @author three
 */
public interface SysMenuService {

  /**
   * 查询系统菜单列表
   *
   * @param dto 菜单信息
   * @return 菜单列表
   */
  List<SysMenu> selectMenuList(MenuDTO dto);

  /**
   * 查询所有菜单列表
   *
   * @param dto MenuDTO
   * @return List
   */
  List<SysMenu> selectAllMenuList(MenuDTO dto);

  /**
   * 增加菜单
   *
   * @param dto OperMenuDTO
   */
  void insertMenu(OperMenuDTO dto);

  /**
   * 编辑菜单
   *
   * @param dto OperMenuDTO
   */
  void updateMenu(OperMenuDTO dto);

  /**
   * 删除菜单
   *
   * @param ids
   */
  void deleteMenuById(List<Long> ids);

  boolean checkMenuNameUnique(OperMenuDTO dto);

  Set<String> getPermissionsByUserId(Long userId);
}
