package com.gameplat.admin.service;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.enums.SysUserEnums;
import com.gameplat.admin.handler.AdminLogBuilder;
import com.gameplat.admin.mapper.SysMenuMapper;
import com.gameplat.admin.mapper.SysUserMapper;
import com.gameplat.admin.model.bean.router.RouterMeta;
import com.gameplat.admin.model.bean.router.VueRouter;
import com.gameplat.base.common.constant.ContextConstant;
import com.gameplat.base.common.util.JwtUtils;
import com.gameplat.common.enums.BooleanEnum;
import com.gameplat.common.enums.SubjectEnum;
import com.gameplat.common.enums.UserTypes;
import com.gameplat.model.entity.sys.SysMenu;
import com.gameplat.model.entity.sys.SysUser;
import com.gameplat.security.SecurityUserHolder;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 系统权限业务层处理
 *
 * @author three
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PermissionService extends ServiceImpl<SysMenuMapper, SysMenu> {

  private final SysMenuMapper menuMapper;

  private final SysUserMapper userMapper;

  private final AdminLogBuilder adminLogBuilder;

  @SentinelResource(value = "getMenuList")
  public ArrayList<VueRouter<SysMenu>> getMenuList(String userName) {
    SysUser user = userMapper.selectUserByUserName(userName);
    if (Objects.isNull(user)) {
      return Lists.newArrayList();
    }

    List<VueRouter<SysMenu>> vueRouters =
        this.getMenuList(user).stream().map(this::convert2VueRouter).collect(Collectors.toList());

    return buildVueRouter(vueRouters, 0L);
  }

  private VueRouter<SysMenu> convert2VueRouter(SysMenu menu) {
    VueRouter<SysMenu> router = new VueRouter<>();
    router.setMenuId(menu.getMenuId());
    router.setParentId(menu.getParentId());
    router.setMeta(new RouterMeta());
    router.setPath(menu.getPath());
    router.setName(menu.getMenuName());
    router.setPerms(menu.getPerms());
    router.setTitle(menu.getTitle());
    router.setMenuType(menu.getMenuType());
    router.setComponent(menu.getComponent());
    router.setIcon(menu.getIcon());
    router.setUrl(menu.getUrl());
    return router;
  }

  public String getAccessLogToken() {
    String username = SecurityUserHolder.getUsername();
    Map<String, String> map = new HashMap<>();
    UserTypes type;
    if (SecurityUserHolder.isSuperAdmin()) {
      type = UserTypes.ADMIN;
    } else {
      type = UserTypes.SUBUSER;
    }
    map.put(ContextConstant.TENANT, adminLogBuilder.getDbSuffix());
    map.put(ContextConstant.USERNAME, username);
    map.put(ContextConstant.USER_TYPE, type.key());
    map.put(ContextConstant.SUBJECT, SubjectEnum.ADMIN.getKey());
    // TODO 暂时写死日志访问权限
    map.put(ContextConstant.AUTHORITY, "operator:logs:logininfoLog,operator:logs:operationLog");
    String secret = JwtUtils.getDefaultSecret();
    return JwtUtils.sign(secret, SecurityUserHolder.getCredential().getTokenExpireIn(), map);
  }

  private List<SysMenu> getMenuList(SysUser user) {
    List<SysMenu> menuList;
    if (SysUserEnums.UserType.isAdmin(user.getUserType())) {
      menuList = menuMapper.selectMenuNormalAll();
    } else {
      menuList = menuMapper.selectMenusByUserId(user.getUserId());
    }

    menuList.removeIf(e -> BooleanEnum.YES.match(e.getVisible()));
    return CollUtil.isNotEmpty(menuList) ? menuList : Lists.newArrayList();
  }

  /**
   * 构造前端路由
   *
   * @param list list
   * @param parentId Long
   * @return ArrayList
   */
  private ArrayList<VueRouter<SysMenu>> buildVueRouter(
      List<VueRouter<SysMenu>> list, Long parentId) {
    return new ArrayList<>(this.getMenuTreeList(list, parentId));
  }

  /**
   * 获取菜单树列表
   *
   * @param list List
   * @param parentId Long
   * @return List
   */
  private List<VueRouter<SysMenu>> getMenuTreeList(List<VueRouter<SysMenu>> list, Long parentId) {
    return list.stream()
        .filter(e -> parentId.equals(e.getParentId()))
        .peek(e -> e.setChildren(this.getMenuTreeList(list, e.getMenuId())))
        .collect(Collectors.toList());
  }
}
