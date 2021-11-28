package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.ActivityInfo;
import com.gameplat.admin.model.dto.ActivityInfoAddDTO;
import com.gameplat.admin.model.dto.ActivityInfoDTO;
import com.gameplat.admin.model.vo.ActivityInfoVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ActivityInfoConvert {

    ActivityInfoVO toVo(ActivityInfo activityInfo);

    ActivityInfo toEntity(ActivityInfoDTO activityInfoDTO);

    ActivityInfo toEntity(ActivityInfoAddDTO activityInfoAddDTO);

}
