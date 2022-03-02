package com.gameplat.admin.convert;

import com.gameplat.admin.model.vo.GameTransferRecordVO;
import com.gameplat.model.entity.game.GameTransferRecord;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper(componentModel = "spring")
public interface GameTransferRecordConvert {

  GameTransferRecordVO toVo(GameTransferRecord entity);
}
