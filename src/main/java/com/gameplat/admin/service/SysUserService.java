package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.SysUser;
import com.gameplat.admin.model.dto.GoogleAuthDTO;
import com.gameplat.admin.model.dto.OperUserDTO;
import com.gameplat.admin.model.dto.UserDTO;
import com.gameplat.admin.model.dto.UserResetPasswordDTO;
import com.gameplat.admin.model.vo.RoleVo;
import com.gameplat.admin.model.vo.UserVo;

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
   * 修改账号状态
   *
   * @param id Long
   * @param status Integer
   */
  void changeStatus(Long id, Integer status);

  /**
   * 用户绑定谷歌密钥
   *
   * @param authDTO GoogleAuthDTO
   */
  void bindSecret(GoogleAuthDTO authDTO);

  /**
   * 校验用户名称是否唯一
   *
   * @param loginName String
   * @return boolean
   */
  boolean checkLoginNameUnique(String loginName);
}
