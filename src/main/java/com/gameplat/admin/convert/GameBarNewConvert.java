package com.gameplat.admin.convert;

import com.gameplat.admin.model.vo.GameBarNewVO;
import com.gameplat.model.entity.game.GameBarNew;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GameBarNewConvert {

    GameBarNewVO toVo(GameBarNew bar);


}
