package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.GameBlacklist;
import com.gameplat.admin.model.dto.OperGameBlacklistDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GameBlacklistConvert {

  GameBlacklist toEntity(OperGameBlacklistDTO operLiveBlacklistDTO);
}
