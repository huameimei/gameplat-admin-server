package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.OperAccountBlacklistDTO;
import com.gameplat.model.entity.blacklist.AccountBlacklist;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountBlacklistConvert {

  AccountBlacklist toEntity(OperAccountBlacklistDTO operAccountBlacklistDTO);
}
