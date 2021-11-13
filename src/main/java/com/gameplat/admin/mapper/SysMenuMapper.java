package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.domain.SysMenu;

import java.util.List;
import java.util.Set;

/**
 * 菜单表 数据层
 *
 * @author three
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {

  /**
   * 根据用户ID查询菜单
   *
   * @param userId 用户ID
   * @return 菜单列表
   */
  List<SysMenu> selectMenusByUserId(Long userId);

  /**
   * 获取用户权限标识
   *
   * @param id Long
   * @return Set
   */
  Set<String> getPermissionsByUserId(Long id);

  /**
   * 查询系统所有菜单（含按钮）
   *
   * @return 菜单列表
   */
  List<SysMenu> selectMenuAll();

  /**
   * 查询系统正常显示菜单（不含按钮）
   *
   * @return 菜单列表
   */
  List<SysMenu> selectMenuNormalAll();

  /**
   * 根据用户ID查询权限
   *
   * @param userId 用户ID
   * @return 权限列表
   */
  List<String> selectPermsByUserId(Long userId);

  /**
   * 根据角色ID查询菜单
   *
   * @param roleId 角色ID
   * @return 菜单列表
   */
  List<String> selectMenuTree(Long roleId);

  /**
   * 查询系统菜单列表
   *
   * @param menu 菜单信息
   * @return 菜单列表
   */
  List<SysMenu> selectMenuList(SysMenu menu);

  /**
   * 新增菜单信息
   *
   * @param menu 菜单信息
   * @return 结果
   */
  int insertMenu(SysMenu menu);

  /**
   * 修改菜单信息
   *
   * @param menu 菜单信息
   * @return 结果
   */
  int updateMenu(SysMenu menu);

  /**
   * 删除菜单管理信息
   *
   * @param menuId 菜单ID
   * @return 结果
   */
  int deleteMenuByIds(Long[] ids);

  /**
   * 校验菜单名称是否唯一
   *
   * @param menu 菜单名称 父菜单ID
   * @return
   */
  SysMenu checkMenuNameUnique(SysMenu menu);
}
