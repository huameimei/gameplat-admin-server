package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.OperGameTypeDTO;
import com.gameplat.admin.model.vo.GameTypeVO;
import com.gameplat.model.entity.game.GameType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GameTypeConvert {

  GameType toEntity(OperGameTypeDTO dto);

  GameTypeVO toVo(GameType gameType);
}
