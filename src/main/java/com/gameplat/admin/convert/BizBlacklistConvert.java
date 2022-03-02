package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.OperBizBlacklistDTO;
import com.gameplat.model.entity.blacklist.BizBlacklist;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BizBlacklistConvert {

  BizBlacklist toEntity(OperBizBlacklistDTO dto);
}
