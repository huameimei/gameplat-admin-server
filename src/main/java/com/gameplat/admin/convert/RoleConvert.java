package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.OperRoleDTO;
import com.gameplat.admin.model.vo.RoleVo;
import com.gameplat.model.entity.sys.SysRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleConvert {

  @Mapping(source = "roleId", target = "id")
  RoleVo toVo(SysRole entity);

  @Mapping(source = "id", target = "roleId")
  SysRole toEntity(OperRoleDTO dto);
}
