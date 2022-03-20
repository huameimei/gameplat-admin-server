package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.OperGameBlacklistDTO;
import com.gameplat.model.entity.game.GameBlacklist;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GameBlacklistConvert {

  GameBlacklist toEntity(OperGameBlacklistDTO dto);
}
