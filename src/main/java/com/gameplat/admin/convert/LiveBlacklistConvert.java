package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.LiveBlacklist;
import com.gameplat.admin.model.dto.OperLiveBlacklistDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LiveBlacklistConvert {

  LiveBlacklist toEntity(OperLiveBlacklistDTO operLiveBlacklistDTO);
}
