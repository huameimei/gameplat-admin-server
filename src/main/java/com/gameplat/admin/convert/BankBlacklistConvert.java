package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.BankBlacklist;
import com.gameplat.admin.model.dto.OperBankBlacklistDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BankBlacklistConvert {

  BankBlacklist toEntity(OperBankBlacklistDTO operBankBlacklistDTO);
}
