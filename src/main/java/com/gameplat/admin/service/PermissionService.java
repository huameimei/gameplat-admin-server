package com.gameplat.admin.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.enums.SysUserEnums;
import com.gameplat.admin.handler.AdminLogBuilder;
import com.gameplat.admin.mapper.SysMenuMapper;
import com.gameplat.admin.mapper.SysUserMapper;
import com.gameplat.admin.model.bean.router.RouterMeta;
import com.gameplat.admin.model.bean.router.VueRouter;
import com.gameplat.admin.model.domain.SysMenu;
import com.gameplat.admin.model.domain.SysUser;
import com.gameplat.base.common.constant.ContextConstant;
import com.gameplat.base.common.util.JwtUtils;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.constant.Constant;
import com.gameplat.common.enums.BooleanEnum;
import com.gameplat.common.enums.SubjectEnum;
import com.gameplat.common.enums.UserTypes;
import com.gameplat.security.SecurityUserHolder;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

  /**
   * 取系统授权菜单
   *
   * @return ArrayList
   */
  @SentinelResource(value = "getMenuList")
  public ArrayList<VueRouter<SysMenu>> getMenuList(String userName) {
    SysUser user = userMapper.selectUserByUserName(userName);
    if (StringUtils.isNull(user)) {
      return Lists.newArrayList();
    }
    List<SysMenu> list;
    if (SysUserEnums.UserType.isAdmin(user.getUserType())) {
      list = menuMapper.selectMenuNormalAll();
    } else {
      list = menuMapper.selectMenusByUserId(user.getUserId());
    }
    if (StringUtils.isNull(list)) {
      return Lists.newArrayList();
    }
    List<VueRouter<SysMenu>> vueRouters = new ArrayList<>();
    list.forEach(
        item -> {
          // 隐藏的菜单分配了不能展示出来
          if (BooleanEnum.NO.match(item.getVisible())) {
            VueRouter<SysMenu> router = new VueRouter<>();
            router.setMenuId(item.getMenuId());
            router.setParentId(item.getParentId());
            router.setMeta(new RouterMeta());
            router.setPath(item.getPath());
            router.setName(item.getMenuName());
            router.setPerms(item.getPerms());
            router.setTitle(item.getTitle());
            router.setMenuType(item.getMenuType());
            router.setComponent(item.getComponent());
            router.setIcon(item.getIcon());
            router.setUrl(item.getUrl());
            vueRouters.add(router);
          }
        });
    return buildVueRouter(vueRouters, 0L);
  }


  public String getAccessLogToken(){
      String username = SecurityUserHolder.getUsername();
      Map<String,String> map = new HashMap<>();
      UserTypes type = null;
      if(SecurityUserHolder.isSuperAdmin()){
          type = UserTypes.ADMIN;
      }else{
          type = UserTypes.SUBUSER;
      }
//      map.put(ContextConstant.TENANT,adminLogBuilder.getDbSuffix());
      map.put(ContextConstant.USERNAME,username);
      map.put(ContextConstant.USER_TYPE,type.key());
      map.put(ContextConstant.SUBJECT, SubjectEnum.ADMIN.getKey());
      //TODO 暂时写死日志访问权限
      map.put(ContextConstant.AUTHORITY,"operator:logs:logininfoLog,operator:logs:operationLog");
      String secret = JwtUtils.getDefaultSecret();
      String token = JwtUtils.sign(secret, SecurityUserHolder.getCredential().getTokenExpireIn(), map);
      return token;
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
    List<VueRouter<SysMenu>> topRoutes = getMenuTreeList(list, 0L);
    return new ArrayList<>(topRoutes);
  }

  /**
   * 获取菜单树列表
   *
   * @param list List
   * @param parentId Long
   * @return List
   */
  private List<VueRouter<SysMenu>> getMenuTreeList(List<VueRouter<SysMenu>> list, Long parentId) {
    List<VueRouter<SysMenu>> routers = new ArrayList<>();
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
