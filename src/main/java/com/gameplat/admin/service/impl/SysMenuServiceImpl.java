package com.gameplat.admin.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.MenuConvert;
import com.gameplat.admin.mapper.SysMenuMapper;
import com.gameplat.admin.model.dto.MenuDTO;
import com.gameplat.admin.model.dto.OperMenuDTO;
import com.gameplat.admin.service.SysMenuService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.model.entity.sys.SysMenu;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 系统菜单 业务层处理
 *
 * @author three
 */
@Slf4j
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu>
    implements SysMenuService {

  @Autowired private SysMenuMapper menuMapper;

  @Autowired private MenuConvert menuConvert;

  @Override
  @SentinelResource(value = "selectMenuList", fallback = "sentineFallBack")
  public List<SysMenu> selectMenuList(MenuDTO menuDTO) {
    SysMenu menu = menuConvert.toEntity(menuDTO);
    List<SysMenu> list = menuMapper.selectMenuList(menu);
    if (StringUtils.isNotNull(list)) {
      return getMenuTreeList(
          list, list.stream().map(SysMenu::getParentId).findFirst().orElse(null));
    }
    return null;
  }

  @Override
  @SentinelResource(value = "selectAllMenuList", fallback = "sentineFallBack")
  public List<SysMenu> selectAllMenuList(MenuDTO menuDTO) {
    SysMenu menu = menuConvert.toEntity(menuDTO);
    List<SysMenu> list = menuMapper.selectMenuList(menu);
    if (StringUtils.isNotNull(list)) {
      return getMenuTreeList(
          list, list.stream().map(SysMenu::getParentId).findFirst().orElse(null));
    }
    return null;
  }

  @Override
  @SentinelResource(value = "insertMenu")
  public void insertMenu(OperMenuDTO menuDTO) {
    SysMenu menu = menuConvert.toEntity(menuDTO);
    if (StringUtils.isNotNull(menuMapper.checkMenuNameUnique(menu))) {
      throw new ServiceException("菜单已存在");
    }

    if (!this.save(menu)) {
      throw new ServiceException("添加菜单失败!");
    }
  }

  @Override
  @SentinelResource(value = "updateMenu")
  public void updateMenu(OperMenuDTO dto) {
    SysMenu menu = menuConvert.toEntity(dto);
    if (!this.updateById(menu)) {
      throw new ServiceException("更新菜单失败!");
    }
  }

  @Override
  @SentinelResource(value = "deleteMenuById")
  public void deleteMenuById(List<Long> ids) {
    if (!this.removeByIds(ids)) {
      throw new ServiceException("批量删除菜单失败!");
    }
  }

  @Override
  @SentinelResource(value = "checkMenuNameUnique")
  public boolean checkMenuNameUnique(OperMenuDTO menuDTO) {
    SysMenu menu = menuConvert.toEntity(menuDTO);
    return StringUtils.isNull(menuMapper.checkMenuNameUnique(menu));
  }

  @Override
  @SentinelResource(value = "getPermissionsByUserId")
  public Set<String> getPermissionsByUserId(Long userId) {
    return menuMapper.getPermissionsByUserId(userId);
  }

  private List<SysMenu> getMenuTreeList(List<SysMenu> list, Long parentId) {
    List<SysMenu> routers = new ArrayList<>();
    list.stream()
        .filter(d -> String.valueOf(parentId).equals(String.valueOf(d.getParentId())))
        .collect(Collectors.toList())
        .forEach(
            item -> {
              item.setChildren(getMenuTreeList(list, item.getMenuId()));
              routers.add(item);
            });
    return routers;
  }
}
