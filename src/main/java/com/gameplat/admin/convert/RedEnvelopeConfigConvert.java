package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.RedEnvelopeConfigDTO;
import com.gameplat.model.entity.recharge.RedEnvelopeConfig;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RedEnvelopeConfigConvert {

    RedEnvelopeConfig dtoToEntity(RedEnvelopeConfigDTO dto);
}
