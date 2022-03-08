package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.OperUserDTO;
import com.gameplat.admin.model.dto.UserDTO;
import com.gameplat.admin.model.dto.UserResetPasswordDTO;
import com.gameplat.admin.model.vo.RoleVo;
import com.gameplat.admin.model.vo.UserVo;
import com.gameplat.model.entity.sys.SysUser;

import java.util.List;

/**
 * 用户 业务层处理
 *
 * @author three
 */
public interface SysUserService extends IService<SysUser> {

  SysUser getByUsername(String username);

  /**
   * 根据条件分页查询用户列表
   *
   * @param userDTO UserDTO
   * @return IPage
   */
  IPage<UserVo> selectUserList(PageDTO<SysUser> page, UserDTO userDTO);

  List<SysUser> getUserByRoleId(Long id);

  /**
   * 新增用户信息
   *
   * @param userDTO OperUserDTO
   */
  void insertUser(OperUserDTO userDTO);

  /**
   * 修改用户信息
   *
   * @param userDTO OperUserDTO
   */
  void updateUser(OperUserDTO userDTO);

  /**
   * 删除用户信息
   *
   * @param ids Long
   */
  void deleteUserById(Long ids);

  List<RoleVo> getRoleList();

  /**
   * 重置用户密码
   *
   * @param userDTO OperUserDTO
   */
  void resetUserPassword(UserResetPasswordDTO dto);

  /**
   * 重置谷歌验证码
   *
   * @param userDTO OperUserDTO
   */
  void resetGoogleSecret(Long id);

  /**
   * 获取安全码
   *
   * @param id Long
   * @return String
   */
  String getSecret(Long id);

  /**
   * 检查安全码是否存在
   *
   * @param secret String
   * @return String
   */
  boolean isSecretExist(String secret);

  /**
   * 修改账号状态
   *
   * @param id Long
   * @param status Integer
   */
  void changeStatus(Long id, Integer status);

  /**
   * 绑定安全码
   *
   * @param id Long
   * @param secret String
   */
  void bindSecret(Long id, String secret);
}
