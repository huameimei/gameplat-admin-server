package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.GameTransferRecord;
import com.gameplat.admin.model.vo.GameTransferRecordVO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper(componentModel = "spring")
public interface GameTransferRecordConvert {

  GameTransferRecordVO toVo(GameTransferRecord liveTransferRecord);
}
