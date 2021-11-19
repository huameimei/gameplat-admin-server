package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.LiveRebatePeriod;
import com.gameplat.admin.model.dto.OperLiveRebatePeriodDTO;
import com.gameplat.admin.model.vo.LiveRebatePeriodVO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper(componentModel = "spring")
public interface LiveRebatePeriodConvert {

  LiveRebatePeriod toEntity(OperLiveRebatePeriodDTO dto);

  LiveRebatePeriodVO toVo(LiveRebatePeriod liveRebatePeriod);
}
