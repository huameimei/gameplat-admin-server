package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.GameKind;
import com.gameplat.admin.model.domain.GameLottery;
import com.gameplat.admin.model.dto.OperGameKindDTO;
import com.gameplat.admin.model.vo.GameKindVO;
import com.gameplat.admin.model.vo.GameLotteryVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GameLotteryConvert {

    GameLotteryVo toVo(GameLottery gameLottery);
}
