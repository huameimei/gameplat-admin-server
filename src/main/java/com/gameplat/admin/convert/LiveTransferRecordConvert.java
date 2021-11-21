package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.LiveTransferRecord;
import com.gameplat.admin.model.vo.LiveTransferRecordVO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper(componentModel = "spring")
public interface LiveTransferRecordConvert {

  LiveTransferRecordVO toVo(LiveTransferRecord liveTransferRecord);
}
