package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.SysMenu;
import com.gameplat.admin.model.dto.MenuDTO;
import com.gameplat.admin.model.dto.OperMenuDTO;
import com.gameplat.admin.model.vo.MenuVo;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface MenuConvert {

  SysMenu toEntity(MenuDTO menuDTO);

  SysMenu toEntity(OperMenuDTO menuDTO);

  MenuVo toVo(SysMenu menu);
}
