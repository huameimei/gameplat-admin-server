package com.gameplat.admin.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.cache.AdminCache;
import com.gameplat.admin.constant.RsaConstant;
import com.gameplat.admin.convert.UserConvert;
import com.gameplat.admin.mapper.SysRoleMapper;
import com.gameplat.admin.mapper.SysUserMapper;
import com.gameplat.admin.mapper.SysUserRoleMapper;
import com.gameplat.admin.model.bean.AdminLoginLimit;
import com.gameplat.admin.model.bean.UserSetting;
import com.gameplat.admin.model.domain.SysRole;
import com.gameplat.admin.model.domain.SysUser;
import com.gameplat.admin.model.domain.SysUserRole;
import com.gameplat.admin.model.dto.GoogleAuthDTO;
import com.gameplat.admin.model.dto.OperUserDTO;
import com.gameplat.admin.model.dto.UserDTO;
import com.gameplat.admin.model.dto.UserResetPasswordDTO;
import com.gameplat.admin.model.vo.RoleVo;
import com.gameplat.admin.model.vo.UserVo;
import com.gameplat.admin.service.SysCommonService;
import com.gameplat.admin.service.SysUserService;
import com.gameplat.common.enums.SystemCodeType;
import com.gameplat.common.enums.UserTypes;
import com.gameplat.common.exception.ServiceException;
import com.gameplat.common.json.JsonUtils;
import com.gameplat.common.util.BeanUtils;
import com.gameplat.common.util.GoogleAuthenticator;
import com.gameplat.common.util.RSAUtils;
import com.gameplat.common.util.StringUtils;
import com.gameplat.security.SecurityUserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
    implements SysUserService {

  @Autowired private SysCommonService commonService;

  @Autowired private SysUserMapper userMapper;

  @Autowired private PasswordEncoder passwordEncoder;

  @Autowired private SysRoleMapper roleMapper;

  @Autowired private SysUserRoleMapper userRoleMapper;

  @Autowired private AdminCache adminCache;

  @Autowired private UserConvert userConvert;

  @Override
  public SysUser getByUsername(String username) {
    return this.lambdaQuery().eq(SysUser::getUserName, username).one();
  }

  @Override
  @SentinelResource(value = "selectUserList")
  public IPage<UserVo> selectUserList(PageDTO<SysUser> page, UserDTO userDTO) {
    LambdaQueryWrapper<SysUser> queryWrapper = Wrappers.lambdaQuery();
    queryWrapper
        .like(
            ObjectUtils.isNotEmpty(userDTO.getAccount()),
            SysUser::getUserName,
            userDTO.getAccount())
        .like(
            ObjectUtils.isNotEmpty(userDTO.getNickName()),
            SysUser::getNickName,
            userDTO.getNickName())
        .eq(
            ObjectUtils.isNotNull(userDTO.getUserType()),
            SysUser::getUserType,
            userDTO.getUserType())
        .eq(ObjectUtils.isNotNull(userDTO.getStatus()), SysUser::getStatus, userDTO.getStatus())
        .eq(ObjectUtils.isNotEmpty(userDTO.getPhone()), SysUser::getPhone, userDTO.getPhone())
        .between(
            ObjectUtils.isNotEmpty(userDTO.getBeginTime()),
            SysUser::getCreateTime,
            userDTO.getBeginTime(),
            userDTO.getEndTime());

    return userMapper.selectUserList(page, queryWrapper).convert(userConvert::toUserVo);
  }

  @Override
  @SentinelResource(value = "insertUser")
  public void insertUser(OperUserDTO dto) {
    if (this.lambdaQuery().eq(SysUser::getUserName, dto.getAccount()).exists()) {
      throw new ServiceException("账号已存在");
    }

    AdminLoginLimit limit = commonService.getLoginLimit();
    if (null == limit) {
      throw new ServiceException("登录配置信息未配置");
    }

    SysUser user = userConvert.toEntity(dto);
    if (UserTypes.ADMIN.match(dto.getUserType())) {
      // 超级管理员但不是特殊的管理员统一设置roleId=1
      if (StringUtils.isNull(dto.getRoleId())) {
        user.setRoleId(1L);
      }
    }

    // 初始化账号设置信息
    UserSetting setting = new UserSetting();
    user.setSettings(JsonUtils.toJson(setting));

    // 生成密码
    String rsaPassword = RSAUtils.decrypt(dto.getPassword(), RsaConstant.PRIVATE_KEY);
    user.setPassword(passwordEncoder.encode(rsaPassword));
    user.setChangeFlag(limit.getChangeFlag() == 1 ? 0 : 1);

    if (this.save(user)) {
      // 删除用户角色表
      userRoleMapper.deleteUserRoleByUserId(user.getUserId());
      insertUserRole(user.getUserId(), user.getRoleId());
    } else {
      throw new ServiceException("添加用户失败!");
    }
  }

  @Override
  @SentinelResource(value = "updateUser")
  public void updateUser(OperUserDTO dto) {
    SysUser user = userConvert.toEntity(dto);
    if (!this.updateById(user)) {
      throw new ServiceException("修改用户信息失败!");
    }

    // 删除用户角色表
    userRoleMapper.deleteUserRoleByUserId(user.getUserId());
    insertUserRole(user.getUserId(), user.getRoleId());
  }

  @Override
  @SentinelResource(value = "deleteUserById")
  public void deleteUserById(Long id) {
    if (1 == id) {
      throw new ServiceException("不允许删除超级管理员用户");
    }

    if (id.equals(SecurityUserHolder.getUserId())) {
      throw new ServiceException("不允许操作自己账号");
    }

    // 删除用户角色表
    userRoleMapper.deleteUserRole(new Long[] {id});
    if (!this.removeById(id)) {
      throw new ServiceException("删除用户失败!");
    }
  }

  @Override
  @SentinelResource(value = "getRoleList")
  public List<RoleVo> getRoleList() {
    List<RoleVo> list = new ArrayList<>();
    LambdaQueryWrapper<SysRole> queryWrapper = Wrappers.lambdaQuery();
    queryWrapper.eq(SysRole::getStatus, SystemCodeType.ENABLE.getCode());
    roleMapper
        .selectList(queryWrapper)
        .forEach(
            item -> {
              RoleVo roleVo = new RoleVo();
              roleVo.setId(item.getRoleId());
              roleVo.setRoleName(item.getRoleName());
              roleVo.setRemark(item.getRemark());
              list.add(roleVo);
            });
    return list;
  }

  @Override
  @SentinelResource(value = "resetUserPassword")
  public void resetUserPassword(UserResetPasswordDTO dto) {
    SysUser user = userConvert.toEntity(dto);
    AdminLoginLimit loginLimit = commonService.getLoginLimit();

    // RSA私钥解密
    String newPassword = RSAUtils.decrypt(dto.getPassword(), RsaConstant.PRIVATE_KEY);
    user.setPassword(passwordEncoder.encode(newPassword));
    user.setChangeFlag(loginLimit.getChangeFlag() == 1 ? 0 : 1);

    if (this.updateById(user)) {
      // 重置用户密码错误次数
      adminCache.cleanErrorPasswordCount(user.getUserName());
    } else {
      throw new ServiceException("更新用户信息失败!");
    }
  }

  @Override
  @SentinelResource(value = "resetGoogleSecret")
  public void resetGoogleSecret(Long id) {
    SysUser user = this.getById(id);
    if (null == user) {
      throw new ServiceException("用户不存在!");
    }

    user.setSafeCode(null);
    if (!this.updateById(user)) {
      throw new ServiceException("重置安全码失败!");
    }
  }

  @Override
  @SentinelResource(value = "resetGoogleSecret")
  public void changeStatus(Long id, Integer status) {
    if (id.equals(SecurityUserHolder.getUserId())) {
      throw new ServiceException("不允许操作自己账号");
    }

    SysUser user = this.getById(id);
    if (null == user) {
      throw new ServiceException("用户不存在!");
    }

    user.setStatus(status);
    if (!this.updateById(user)) {
      throw new ServiceException("修改状态失败!");
    }
  }

  @Override
  @SentinelResource(value = "bindSecret")
  public void bindSecret(GoogleAuthDTO authDTO) {
    // 先校验验证码是否正确
    Boolean authcode = GoogleAuthenticator.authcode(authDTO.getSafeCode(), authDTO.getSecret());
    if (!authcode) {
      throw new ServiceException("安全码错误");
    }
    SysUser user = userMapper.selectUserByUserName(authDTO.getLoginName());
    if (StringUtils.isNull(user)) {
      throw new ServiceException("账号错误");
    }

    if (!this.updateById(
        new SysUser() {
          {
            setUserId(user.getUserId());
            setSafeCode(authDTO.getSecret());
          }
        })) {
      throw new ServiceException("绑定失败!");
    }
  }

  @Override
  @SentinelResource(value = "checkLoginNameUnique")
  public boolean checkLoginNameUnique(String loginName) {
    SysUser user = userMapper.selectUserByUserName(loginName);
    return StringUtils.isNull(user);
  }

  /**
   * 新增用户角色信息
   *
   * @param userId Long
   * @param roleId Long
   */
  private void insertUserRole(Long userId, Long roleId) {
    if (StringUtils.isNotNull(roleId)) {
      // 新增用户与角色管理
      List<SysUserRole> list = new ArrayList<>();
      SysUserRole ur = new SysUserRole();
      ur.setUserId(userId);
      ur.setRoleId(roleId);
      list.add(ur);
      userRoleMapper.batchUserRole(list);
    }
  }
}
