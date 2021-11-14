package com.gameplat.admin.service;

import cn.hutool.json.JSONObject;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.gameplat.admin.constant.RsaConstant;
import com.gameplat.admin.enums.SysUserEnums;
import com.gameplat.admin.mapper.SysMenuMapper;
import com.gameplat.admin.mapper.SysUserMapper;
import com.gameplat.admin.model.domain.SysUser;
import com.gameplat.admin.model.dto.ChangePasswordDTO;
import com.gameplat.admin.model.dto.UserSettingDTO;
import com.gameplat.admin.model.vo.ProFileVo;
import com.gameplat.common.enums.SystemCodeType;
import com.gameplat.common.exception.ServiceException;
import com.gameplat.common.json.JsonUtils;
import com.gameplat.common.security.SecurityUserHolder;
import com.gameplat.common.util.RSAUtils;
import com.gameplat.common.util.StringUtils;
import com.gameplat.security.service.JwtTokenService;
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

  @Autowired private SysUserMapper userMapper;

  @Autowired private SysMenuMapper menuMapper;

  @Autowired private JwtTokenService jwtTokenService;

  @Autowired private PasswordEncoder passwordEncoder;

  /**
   * 取用户信息
   *
   * @return
   */
  @SentinelResource(value = "current")
  public ProFileVo current(String username) {
    SysUser user = userMapper.selectUserByUserName(username);
    if (StringUtils.isNull(user)) {
      log.info("未找到用户信息[{}]", username);
      throw new ServiceException("未找到用户信息");
    }

    // 删除用户凭证
    if (SystemCodeType.DISABLE.match(user.getStatus())) {
      jwtTokenService.removeToken(username);
      throw new ServiceException("账号已停用");
    }

    // 获取用户权限列表
    List<String> permisList = getPermisList(user);
    ProFileVo userVo = new ProFileVo();
    userVo.setUserName(user.getUserName());
    userVo.setNickName(user.getNickName());
    userVo.setPhone(user.getPhone());
    userVo.setCreateTime(user.getCreateTime());
    userVo.setIsChange(user.getChangeFlag());
    userVo.setPermis(permisList);
    userVo.setSetting(user.getSettings());
    return userVo;
  }

  @SentinelResource(value = "saveUserSetting")
  public void saveUserSetting(UserSettingDTO settingDTO) {
    String username = SecurityUserHolder.getUsername();
    SysUser user = userMapper.selectUserByUserName(username);
    if (StringUtils.isNull(user)) {
      log.info("未找到用户信息[{}]", username);
      throw new ServiceException("未找到用户信息");
    }

    JSONObject json = new JSONObject();
    // 用户设置json
    json.putOnce("indexUrl", settingDTO.getIndexUrl());
    json.putOnce("defaultPageSize", settingDTO.getDefaultPageSize());
    json.putOnce("receiptOrder", settingDTO.getReceiptOrder());
    json.putOnce("withdrawOrder", settingDTO.getWithdrawOrder());
    json.putOnce("thousandsSeparator", settingDTO.getThousandsSeparator());
    json.putOnce("fractionCount", settingDTO.getFractionCount());
    json.putOnce("openDefaultNavMenu", settingDTO.getOpenDefaultNavMenu());
    json.putOnce("specialMemberWarn", settingDTO.getSpecialMemberWarn());
    json.putOnce("themeColor", settingDTO.getThemeColor());
    json.putOnce("tagsView", settingDTO.getTagsView());
    json.putOnce("sidebarLogo", settingDTO.getSidebarLogo());
    json.putOnce("showTimeType", settingDTO.getShowTimeType());
    json.putOnce("sidebarShow", settingDTO.getSidebarShow());
    String setting = JsonUtils.toJson(json);
    user.setSettings(setting);
    user.setNickName(settingDTO.getNickName());
    user.setPhone(settingDTO.getPhone());
    if (userMapper.updateById(user) <= 0) {
      throw new ServiceException("更新用户信息失败!");
    }
  }

  /**
   * 修改密码
   *
   * @param changePassword
   * @return
   */
  @SentinelResource(value = "changePassword")
  public void changePassword(ChangePasswordDTO changePassword) {
    String username = SecurityUserHolder.getUsername();
    SysUser user = userMapper.selectUserByUserName(username);
    if (StringUtils.isNull(user)) {
      log.info("未找到用户信息[{}]", username);
      throw new ServiceException("旧密码不正确");
    }

    // RSA私钥解密
    String oldPassword = RSAUtils.decrypt(changePassword.getOldPassWord(), RsaConstant.PRIVATE_KEY);
    if (StringUtils.isBlank(oldPassword)) {
      throw new ServiceException("旧密码不正确");
    }

    String newPassword = RSAUtils.decrypt(changePassword.getNewPassWord(), RsaConstant.PRIVATE_KEY);
    if (StringUtils.isBlank(newPassword)) {
      throw new ServiceException("新密码不符合规范");
    }

    if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
      throw new ServiceException("旧密码不正确!");
    }

    user.setChangeFlag(SystemCodeType.YES.getCode());
    user.setPassword(passwordEncoder.encode(newPassword));
    if (userMapper.updateById(user) <= 0) {
      throw new ServiceException("密码修改失败!");
    }
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
