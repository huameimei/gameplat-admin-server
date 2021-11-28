package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.SysInformation;
import com.gameplat.admin.model.dto.SysInformationAddDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SysInformationConvert {

    SysInformation toEntity (SysInformationAddDTO dto);
}
