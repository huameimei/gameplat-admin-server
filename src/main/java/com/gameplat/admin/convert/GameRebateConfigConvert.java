package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.OperGameRebateConfigDTO;
import com.gameplat.model.entity.game.GameRebateConfig;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GameRebateConfigConvert {

  GameRebateConfig toEntity(OperGameRebateConfigDTO dto);
}
