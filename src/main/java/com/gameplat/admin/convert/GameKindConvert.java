package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.GameKind;
import com.gameplat.admin.model.dto.OperGameKindDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GameKindConvert {
    GameKind toEntity(OperGameKindDTO dto);
}
