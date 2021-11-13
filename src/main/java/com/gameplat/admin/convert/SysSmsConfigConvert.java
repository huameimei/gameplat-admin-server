package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.SysSmsConfig;
import com.gameplat.admin.model.dto.SysSmsConfigDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SysSmsConfigConvert {

  SysSmsConfig toEntity(SysSmsConfigDTO dto);
}
