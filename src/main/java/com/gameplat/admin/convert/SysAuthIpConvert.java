package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.SysAuthIpAddDTO;
import com.gameplat.admin.model.entity.SysAuthIp;
import com.gameplat.admin.model.vo.SysAuthIpVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SysAuthIpConvert {

    SysAuthIp toEntity(SysAuthIpAddDTO dto);

    SysAuthIpVO toVo(SysAuthIp sysAuthIp);
}
