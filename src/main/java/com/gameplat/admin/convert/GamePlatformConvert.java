package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.GamePlatform;
import com.gameplat.admin.model.dto.OperGamePlatformDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GamePlatformConvert {
    GamePlatform toEntity(OperGamePlatformDTO dto);
}
