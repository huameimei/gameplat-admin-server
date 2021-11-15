package com.gameplat.admin.service.impl;

import com.gameplat.admin.enums.SysUserEnums;
import com.gameplat.admin.model.domain.SysUser;
import com.gameplat.admin.service.SysMenuService;
import com.gameplat.admin.service.SysRoleService;
import com.gameplat.admin.service.SysUserService;
import com.gameplat.security.context.UserCredential;
import com.gameplat.security.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;

@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

  @Autowired private SysUserService userService;

  @Autowired private SysMenuService menuService;

  @Autowired private SysRoleService roleService;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    SysUser user = userService.getByUsername(username);
    if (null == user) {
      throw new UsernameNotFoundException("用户名不存在或密码错误!");
    }

    Set<String> roles = roleService.getRolesByUserId(user.getUserId());

    Set<String> permissions = menuService.getPermissionsByUserId(user.getUserId());
    Collection<? extends GrantedAuthority> authorities =
        SecurityUtil.createAuthorityList(permissions);

    return UserCredential.builder()
        .userId(user.getUserId())
        .status(user.getStatus())
        .username(username)
        .roles(roles)
        .isSuperAdmin(SysUserEnums.UserType.isAdmin(user.getUserType()))
        .password(user.getPassword())
        .authorities(authorities)
        .build();
  }
}
