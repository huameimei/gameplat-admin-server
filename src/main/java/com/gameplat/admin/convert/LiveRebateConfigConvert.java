package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.LiveRebateConfig;
import com.gameplat.admin.model.dto.OperLiveRebateConfigDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LiveRebateConfigConvert {
  LiveRebateConfig toEntity(OperLiveRebateConfigDTO dto);
}
