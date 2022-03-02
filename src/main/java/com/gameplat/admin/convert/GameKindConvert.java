package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.OperGameKindDTO;
import com.gameplat.admin.model.vo.GameKindVO;
import com.gameplat.model.entity.game.GameKind;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GameKindConvert {
  GameKind toEntity(OperGameKindDTO dto);

  GameKindVO toVo(GameKind gameKind);
}
