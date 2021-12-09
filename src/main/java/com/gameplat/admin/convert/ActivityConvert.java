package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.Activity;
import com.gameplat.admin.model.dto.ActivityAddDTO;
import com.gameplat.admin.model.dto.ActivityUpdateDTO;
import com.gameplat.admin.model.vo.ActivityVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ActivityConvert {

    ActivityVO toVo(Activity activity);

    Activity toEntity(ActivityAddDTO activityAddDTO);

    Activity toEntity(ActivityUpdateDTO activityUpdateDTO);

}
