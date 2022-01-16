package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.GameRebateConfig;
import com.gameplat.admin.model.dto.OperGameRebateConfigDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GameRebateConfigConvert {
  GameRebateConfig toEntity(OperGameRebateConfigDTO dto);
}
