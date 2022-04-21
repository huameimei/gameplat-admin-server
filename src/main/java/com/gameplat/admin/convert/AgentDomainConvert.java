package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.AgentDomainDTO;
import com.gameplat.admin.model.vo.AgentDomainVO;
import com.gameplat.model.entity.spread.AgentDomain;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AgentDomainConvert {

    AgentDomainVO toVo(AgentDomain bar);

    AgentDomain toDto(AgentDomainDTO bar);
}
