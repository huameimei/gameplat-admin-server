package com.gameplat.admin.convert;

import com.gameplat.admin.model.entity.SysMenu;
import com.gameplat.admin.model.vo.SysMenuVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SysMenuConvert {

    SysMenuVO toVo(SysMenu sysMenu);
}
