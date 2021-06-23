package com.gameplat.admin.convert;

import com.gameplat.admin.model.entity.SysMenu;
import com.gameplat.admin.model.vo.SysMenuVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SysMenuConvert {

  SysMenuVo toVo(SysMenu sysMenu);

}
