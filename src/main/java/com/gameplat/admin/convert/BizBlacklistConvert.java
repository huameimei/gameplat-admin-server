package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.BizBlacklist;
import com.gameplat.admin.model.dto.OperBizBlacklistDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BizBlacklistConvert {

  BizBlacklist toEntity(OperBizBlacklistDTO operBizBlacklistDTO);
}
