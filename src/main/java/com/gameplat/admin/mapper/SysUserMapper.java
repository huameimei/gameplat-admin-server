package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.domain.SysUser;
import org.apache.ibatis.annotations.Param;

/** 用户 数据层 */
public interface SysUserMapper extends BaseMapper<SysUser> {

  /**
   * 根据条件分页查询用户列表
   *
   * @param page 用户信息
   * @return 用户信息集合信息
   */
  IPage<SysUser> selectUserList(
      Page<SysUser> page, @Param(Constants.WRAPPER) Wrapper<SysUser> wrapper);

  /**
   * 通过用户名查询用户
   *
   * @param userName 用户名
   * @return 用户对象信息
   */
  SysUser selectUserByUserName(String userName);

  /**
   * 重置用户安全码
   *
   * @param user
   * @return
   */
  int resetUserAuth(SysUser user);
}
