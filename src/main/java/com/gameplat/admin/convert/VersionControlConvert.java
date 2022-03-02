package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.VersionControlDTO;
import com.gameplat.model.entity.VersionControl;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VersionControlConvert {
  VersionControl toEntity(VersionControlDTO dto);
}
