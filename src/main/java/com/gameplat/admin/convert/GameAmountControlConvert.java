package com.gameplat.admin.convert;

import com.gameplat.admin.model.vo.GameAmountControlVO;
import com.gameplat.model.entity.game.GameAmountControl;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GameAmountControlConvert {
  GameAmountControlVO toVo(GameAmountControl gameAmountControl);
}
