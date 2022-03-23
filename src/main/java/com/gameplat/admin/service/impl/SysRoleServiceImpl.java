package com.gameplat.admin.service.impl;

import cn.hutool.core.lang.Assert;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.RoleConvert;
import com.gameplat.admin.mapper.SysRoleMapper;
import com.gameplat.admin.mapper.SysRoleMenuMapper;
import com.gameplat.admin.model.dto.AuthMenuDTO;
import com.gameplat.admin.model.dto.OperRoleDTO;
import com.gameplat.admin.model.dto.RoleDTO;
import com.gameplat.admin.model.vo.RoleVo;
import com.gameplat.admin.service.SysRoleService;
import com.gameplat.admin.service.SysUserService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.enums.BooleanEnum;
import com.gameplat.model.entity.sys.SysRole;
import com.gameplat.model.entity.sys.SysRoleMenu;
import com.gameplat.model.entity.sys.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * 角色管理 业务层处理
 *
 * @author three
 */
@Slf4j
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole>
    implements SysRoleService {

  @Autowired private SysRoleMapper roleMapper;

  @Autowired private SysRoleMenuMapper roleMenuMapper;

  @Autowired private RoleConvert roleConvert;

  @Autowired private SysUserService userService;

  @Override
  @SentinelResource(value = "selectGroupList", fallback = "sentineFallBack")
  public IPage<RoleVo> selectGroupList(PageDTO<SysRole> page, RoleDTO dto) {
    return selectRoleList(page, dto);
  }

  @Override
  @SentinelResource(value = "insertGroup", fallback = "sentineFallBack")
  public void insertGroup(OperRoleDTO roleDTO) {
    insertRole(roleDTO);
  }

  @Override
  @SentinelResource(value = "updateGroup", fallback = "sentineFallBack")
  public void updateGroup(OperRoleDTO roleDTO) {
    updateRole(roleDTO);
  }

  @Override
  @SentinelResource(value = "deleteGroupById", fallback = "sentineFallBack")
  public void deleteGroupById(Long id) {
    SysRole role = this.getById(id);
    Assert.notNull(role, "分组不存在");
    // 分组下存在用户不允许删除  bug单号 18894   测试提出
    List<SysUser> userRoles = userService.getUserByRoleId(id);
    Assert.isFalse(userRoles.size() > 0, "分组下存在用户无法删除");
    Assert.isFalse(BooleanEnum.YES.match(role.getDefaultFlag()), "默认分组不能删除");
    if (!this.removeById(id)) {
      throw new ServiceException("删除分组失败!");
    }
  }

  @Override
  @SentinelResource(value = "selectRoleList", fallback = "sentineFallBack")
  public IPage<RoleVo> selectRoleList(PageDTO<SysRole> page, RoleDTO dto) {
    QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
    queryWrapper
            .like(ObjectUtils.isNotEmpty(dto.getRoleName()), "r.role_name", dto.getRoleName())
            .eq(ObjectUtils.isNotNull(dto.getStatus()), "r.status", dto.getStatus())
            .like(ObjectUtils.isNotEmpty(dto.getRoleKey()), "r.role_key", dto.getRoleKey());

    return roleMapper.selectRoleList(page, queryWrapper).convert(roleConvert::toVo);
  }

  @Override
  @SentinelResource(value = "insertRole", fallback = "sentineFallBack")
  public void insertRole(OperRoleDTO dto) {
    SysRole role = roleConvert.toEntity(dto);
    if (StringUtils.isNotNull(roleMapper.checkRoleNameUnique(role))) {
      throw new ServiceException("角色名称已存在");
    }

    if (StringUtils.isNotNull(roleMapper.checkRoleKeyUnique(role))) {
      throw new ServiceException("角色编码已存在");
    }

    if (!this.save(role)) {
      throw new ServiceException("插入失败!");
    }
  }

  @Override
  @SentinelResource(value = "updateRole", fallback = "sentineFallBack")
  public void updateRole(OperRoleDTO dto) {
    SysRole role = roleConvert.toEntity(dto);
    if (StringUtils.isNotNull(roleMapper.checkRoleKeyIdUnique(role))) {
      throw new ServiceException("角色编码已存在");
    }
    if (!this.updateById(role)) {
      throw new ServiceException("更新角色失败!");
    }
  }

  @Override
  @SentinelResource(value = "deleteRoleByIds", fallback = "sentineFallBack")
  public void deleteRoleByIds(String ids) {
    List<String> roleIds = Arrays.asList(StringUtils.split(ids, ","));
    if (!this.removeByIds(roleIds)) {
      throw new ServiceException("批量删除失败!");
    }
  }

  @Override
  @SentinelResource(value = "checkRoleNameUnique", fallback = "sentineFallBack")
  public boolean checkRoleNameUnique(Long id, String groupName) {
    return this.lambdaQuery().eq(SysRole::getRoleId, id).eq(SysRole::getRoleName, groupName).count()
        > 0;
  }

  @Override
  @SentinelResource(value = "checkRoleKeyUnique", fallback = "sentineFallBack")
  public boolean checkRoleKeyUnique(Long id, String roleKey) {
    return this.lambdaQuery().eq(SysRole::getRoleId, id).eq(SysRole::getRoleKey, roleKey).count()
        > 0;
  }

  @Override
  @SentinelResource(value = "getRoleMenuList", fallback = "sentineFallBack")
  public List<Long> getRoleMenuList(Long roleId) {
    return roleMenuMapper.selectRoleMenuList(roleId);
  }

  @Override
  @SentinelResource(value = "authMenuScope", fallback = "sentineFallBack")
  public void authMenuScope(AuthMenuDTO dto) {
    Long roleId = dto.getRoleId();
    roleMenuMapper.deleteRoleMenuByRoleId(roleId);

    List<SysRoleMenu> roleMenuList = new ArrayList<>();
    dto.getMenuIds()
        .forEach(
            menuId -> {
              SysRoleMenu saveObj = new SysRoleMenu();
              saveObj.setRoleId(roleId);
              saveObj.setMenuId(menuId);
              roleMenuList.add(saveObj);
            });

    if (roleMenuMapper.batchRoleMenu(roleMenuList) <= 0) {
      throw new ServiceException("批量更新菜单失败!");
    }
  }

  @Override
  public Set<String> getRolesByUserId(Long userId) {
    return roleMapper.getRolesByUserId(userId);
  }

  @Override
  public void changeDefault(Long id, Integer defaultFlag) {
    if (!this.lambdaUpdate()
        .set(SysRole::getDefaultFlag, defaultFlag)
        .eq(SysRole::getRoleId, id)
        .update()) {
      throw new ServiceException("更新角色失败!");
    }
  }
}
