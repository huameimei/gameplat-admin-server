package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.Game;
import com.gameplat.admin.model.dto.OperGameDTO;
import com.gameplat.admin.model.vo.GameVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GameConvert {
    Game toEntity(OperGameDTO dto);

    GameVO toVo(Game game);
}
