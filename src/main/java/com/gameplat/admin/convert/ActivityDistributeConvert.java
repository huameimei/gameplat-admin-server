package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.ActivityDistributeDTO;
import com.gameplat.admin.model.vo.ActivityDistributeVO;
import com.gameplat.model.entity.activity.ActivityDistribute;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ActivityDistributeConvert {

  ActivityDistribute toEntity(ActivityDistributeDTO activityDistributeDTO);

  ActivityDistributeVO toVo(ActivityDistribute activityDistribute);
}
