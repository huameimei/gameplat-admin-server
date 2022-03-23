package com.gameplat.admin.service.impl;

import com.gameplat.admin.enums.SysUserEnums;
import com.gameplat.admin.service.SysMenuService;
import com.gameplat.admin.service.SysRoleService;
import com.gameplat.admin.service.SysUserService;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.model.entity.sys.SysUser;
import com.gameplat.security.context.UserCredential;
import com.gameplat.security.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

  @Autowired private SysUserService userService;

  @Autowired private SysMenuService menuService;

  @Autowired private SysRoleService roleService;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    SysUser user =
        Optional.ofNullable(userService.getByUsername(username))
            .orElseThrow(() -> new UsernameNotFoundException("用户名不存在或密码错误!"));

    // 判断用户是否被禁用了
    if (SysUserEnums.Status.DISABLED.match(user.getStatus())) {
      throw new LockedException("账号已被禁用，如有疑问请联系管理员");
    }

    Set<String> roles = roleService.getRolesByUserId(user.getUserId());
    Set<String> permissions = menuService.getPermissionsByUserId(user.getUserId());
    Collection<? extends GrantedAuthority> authorities =
        SecurityUtil.createAuthorityList(permissions);

    return UserCredential.builder()
        .userId(user.getUserId())
        .username(username)
        .nickname(user.getNickName())
        .userType(user.getUserType())
        .status(user.getStatus())
        .roles(roles)
        .isSuperAdmin(SysUserEnums.UserType.isAdmin(user.getUserType()))
        .password(user.getPassword())
        .authorities(authorities)
        .isEnable2FA(StringUtils.isNotEmpty(user.getSafeCode()))
        .build();
  }
}
