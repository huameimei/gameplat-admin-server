package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.AccountBlacklist;
import com.gameplat.admin.model.dto.OperAccountBlacklistDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountBlacklistConvert {

  AccountBlacklist toEntity(OperAccountBlacklistDTO operAccountBlacklistDTO);
}
