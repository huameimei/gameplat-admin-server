package com.gameplat.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.entity.SysMenu;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Lenovo
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {


    @Select("select * from sys_menu m inner join sys_role_menu r on m.id = r.menu_Id where r.role_Id = #{roleId} and type = 2 ORDER BY sort ASC  ")
    List<SysMenu> selectMenusByRoleId(Long roleId);

}
