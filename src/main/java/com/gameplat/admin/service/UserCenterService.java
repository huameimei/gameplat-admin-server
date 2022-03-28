package com.gameplat.admin.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.gameplat.admin.convert.UserConvert;
import com.gameplat.admin.enums.SysUserEnums;
import com.gameplat.admin.mapper.SysMenuMapper;
import com.gameplat.admin.model.dto.ChangePasswordDTO;
import com.gameplat.admin.model.dto.UserInfoDTO;
import com.gameplat.admin.model.vo.ProfileVO;
import com.gameplat.common.enums.BooleanEnum;
import com.gameplat.common.lang.Assert;
import com.gameplat.model.entity.sys.SysUser;
import com.gameplat.security.SecurityUserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 个人中心服务
 *
 * @author three
 */
@Slf4j
@Service
public class UserCenterService {

  @Autowired private SysUserService userService;

  @Autowired private SysMenuMapper menuMapper;

  @Autowired private PasswordService passwordService;

  @Autowired private PasswordEncoder passwordEncoder;

  @Autowired private UserConvert userConvert;

  @SentinelResource(value = "current")
  public ProfileVO current(String username) {
    SysUser user = Assert.notNull(userService.getByUsername(username), "未找到用户信息");
    ProfileVO profileVO = userConvert.toProFileVo(user);
    profileVO.setPermis(getPermisList(user));
    return profileVO;
  }

  @SentinelResource(value = "update")
  public void update(UserInfoDTO dto) {
    SysUser user = userConvert.toEntity(dto);
    user.setUserId(SecurityUserHolder.getUserId());
    Assert.isTrue(userService.updateById(user), "更新用户信息失败!");
  }

  @SentinelResource(value = "changePassword")
  public void changePassword(ChangePasswordDTO dto) {
    String username = SecurityUserHolder.getUsername();
    SysUser user = Assert.notNull(userService.getByUsername(username), "旧密码不正确");

    // RSA私钥解密
    String oldPassword = Assert.notEmpty(passwordService.decrypt(dto.getOldPassWord()), "旧密码不正确");
    String newPassword = passwordService.decrypt(dto.getNewPassWord());
    Assert.isTrue(passwordEncoder.matches(oldPassword, user.getPassword()), "旧密码不正确!");

    user.setChangeFlag(BooleanEnum.YES.value());
    user.setPassword(passwordEncoder.encode(newPassword));
    Assert.isTrue(userService.updateById(user), "密码修改失败!");
  }

  /**
   * 获取用户权限列表
   *
   * @param user
   * @return
   */
  @SentinelResource(value = "getPermisList")
  private List<String> getPermisList(SysUser user) {
    List<String> list = new ArrayList<>();
    if (SysUserEnums.UserType.isAdmin(user.getUserType())) {
      list.add("*:*:*");
    } else {
      list = menuMapper.selectPermsByUserId(user.getUserId());
    }
    return list;
  }
}
