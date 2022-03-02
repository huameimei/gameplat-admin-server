package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.OperGamePlatformDTO;
import com.gameplat.model.entity.game.GamePlatform;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GamePlatformConvert {
  GamePlatform toEntity(OperGamePlatformDTO dto);
}
