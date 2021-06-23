package com.gameplat.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.entity.SysRole;
import org.apache.ibatis.annotations.Select;

/**
 * @author Lenovo
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

  @Select("select * from sys_role_user where user_Id = #{userId}")
  SysRole selectRoleByUserId(Long userId);

}
