package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.OperGameRebatePeriodDTO;
import com.gameplat.admin.model.vo.GameRebatePeriodVO;
import com.gameplat.model.entity.game.GameRebatePeriod;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper(componentModel = "spring")
public interface GameRebatePeriodConvert {

  GameRebatePeriod toEntity(OperGameRebatePeriodDTO dto);

  GameRebatePeriodVO toVo(GameRebatePeriod liveRebatePeriod);
}
