package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.ActivityQualification;
import com.gameplat.admin.model.dto.ActivityQualificationAddDTO;
import com.gameplat.admin.model.dto.ActivityQualificationDTO;
import com.gameplat.admin.model.dto.ActivityQualificationUpdateDTO;
import com.gameplat.admin.model.vo.ActivityQualificationVO;
import org.mapstruct.Mapper;

/**
 * 活动资格转换
 */
@Mapper(componentModel = "spring")
public interface ActivityQualificationConvert {

    ActivityQualificationVO toVo(ActivityQualification activityQualification);

    ActivityQualification toEntity(ActivityQualificationDTO activityQualificationDTO);

    ActivityQualification toEntity(ActivityQualificationAddDTO activityQualificationAddDTO);

    ActivityQualification toEntity(ActivityQualificationUpdateDTO activityQualificationUpdateDTO);

}
