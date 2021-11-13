package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.SysAuthIp;
import com.gameplat.admin.model.dto.OperAuthIpDTO;
import com.gameplat.admin.model.vo.AuthIpVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthIpConvert {

  @Mapping(source = "ip", target = "allowIp")
  SysAuthIp toEntity(OperAuthIpDTO operAuthIpDTO);

  @Mapping(source = "allowIp", target = "ip")
  AuthIpVo toVo(SysAuthIp authIp);
}
