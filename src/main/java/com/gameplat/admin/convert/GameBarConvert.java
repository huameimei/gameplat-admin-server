package com.gameplat.admin.convert;

import com.gameplat.admin.model.vo.GameBarVO;
import com.gameplat.model.entity.game.GameBar;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GameBarConvert {

    GameBarVO toVo(GameBar bar);


}
