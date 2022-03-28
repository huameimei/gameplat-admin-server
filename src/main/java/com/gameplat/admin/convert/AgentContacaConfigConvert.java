package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.AgentContacaDTO;
import com.gameplat.model.entity.AgentContacaConfig;
import org.mapstruct.Mapper;

/**
 * @author lily
 * @description
 * @date 2022/1/3
 */
@Mapper(componentModel = "spring")
public interface AgentContacaConfigConvert {
  AgentContacaConfig toEntity(AgentContacaDTO dto);
}
