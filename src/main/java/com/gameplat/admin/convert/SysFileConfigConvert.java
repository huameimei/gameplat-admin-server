package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.SysFileConfig;
import com.gameplat.admin.model.dto.SysFileConfigDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SysFileConfigConvert {

  SysFileConfig toEntity(SysFileConfigDTO dto);
}
