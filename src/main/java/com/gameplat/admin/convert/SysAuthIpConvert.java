package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.SysAuthIpAddDto;
import com.gameplat.admin.model.entity.SysAuthIp;
import com.gameplat.admin.model.vo.SysAuthIpVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SysAuthIpConvert {

  SysAuthIp toEntity(SysAuthIpAddDto dto);

  SysAuthIpVo toVo(SysAuthIp sysAuthIp);

}
