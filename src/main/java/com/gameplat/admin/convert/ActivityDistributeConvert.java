package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.ActivityDistribute;
import com.gameplat.admin.model.dto.ActivityDistributeDTO;
import com.gameplat.admin.model.vo.ActivityDistributeVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ActivityDistributeConvert {

    ActivityDistribute toEntity(ActivityDistributeDTO activityDistributeDTO);

    ActivityDistributeVO toVo(ActivityDistribute activityDistribute);



}
