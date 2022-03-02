package com.gameplat.admin.convert;

import com.gameplat.admin.model.vo.GameLotteryVo;
import com.gameplat.model.entity.game.GameLottery;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GameLotteryConvert {

  GameLotteryVo toVo(GameLottery gameLottery);
}
