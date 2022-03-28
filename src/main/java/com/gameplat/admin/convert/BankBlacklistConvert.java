package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.OperBankBlacklistDTO;
import com.gameplat.model.entity.blacklist.BankBlacklist;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BankBlacklistConvert {

  BankBlacklist toEntity(OperBankBlacklistDTO dto);
}
