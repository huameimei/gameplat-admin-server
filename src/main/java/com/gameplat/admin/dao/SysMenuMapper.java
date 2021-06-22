package com.gameplat.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.entity.SysMenu;
import com.gameplat.admin.model.entity.SysRole;
import java.util.List;
import org.apache.ibatis.annotations.Select;

/**
 * @author Lenovo
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {


  @Select("select distinct t.auth_mark from sys_menu m inner join sys_role_menu r on m.id = r.menu_Id where r.role_Id = #{roleId} and type = 2 ORDER BY sort ASC  ")
   List<String> selectMenuByRoleId(Long roleId);

}
