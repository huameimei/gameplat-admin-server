package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.SysVersionInfo;
import com.gameplat.admin.model.dto.SysVersionInfoDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SysVersionInfoConvert {
    SysVersionInfo toEntity(SysVersionInfoDTO dto);



}
