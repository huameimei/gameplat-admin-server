package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.domain.SysRole;
import java.util.List;
import java.util.Set;
import org.apache.ibatis.annotations.Param;

/**
 * 角色表 数据层
 *
 * @author three
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

  /**
   * 根据条件分页查询角色数据
   *
   * @param page 角色信息
   * @return 角色数据集合信息
   */
  IPage<SysRole> selectRoleList(
      Page<SysRole> page, @Param(Constants.WRAPPER) Wrapper<SysRole> wrapper);

  /**
   * 根据Id查找角色
   *
   * @param roleId
   * @return
   */
  SysRole selectRoleById(Long roleId);

  /**
   * 根据用户ID查询角色
   *
   * @param userId 用户ID
   * @return 角色列表
   */
  List<SysRole> selectRolesByUserId(Long userId);

  /**
   * 新增角色信息
   *
   * @param role 角色信息
   * @return 结果
   */
  int insertRole(SysRole role);

  /**
   * 修改角色信息
   *
   * @param role 角色信息
   * @return 结果
   */
  int updateRole(SysRole role);

  /**
   * 批量角色用户信息
   *
   * @param ids 需要删除的数据ID
   * @return 结果
   */
  int deleteRoleByIds(Long[] ids);

  /**
   * 校验角色名称是否唯一
   *
   * @param role 角色名称
   * @return 角色信息
   */
  SysRole checkRoleNameUnique(SysRole role);

  /**
   * 校验角色权限是否唯一
   *
   * @param role 角色
   * @return 角色信息
   */
  SysRole checkRoleKeyUnique(SysRole role);

  Set<String> getRolesByUserId(Long userId);
}
