package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.ActivityQualificationAddDTO;
import com.gameplat.admin.model.dto.ActivityQualificationDTO;
import com.gameplat.admin.model.dto.ActivityQualificationUpdateDTO;
import com.gameplat.admin.model.dto.ActivityQualificationUpdateStatusDTO;
import com.gameplat.admin.model.vo.ActivityQualificationVO;
import com.gameplat.model.entity.activity.ActivityQualification;
import org.mapstruct.Mapper;

/** 活动资格转换 */
@Mapper(componentModel = "spring")
public interface ActivityQualificationConvert {

  ActivityQualificationVO toVo(ActivityQualification entity);

  ActivityQualification toEntity(ActivityQualificationDTO dto);

  ActivityQualification toEntity(ActivityQualificationAddDTO dto);

  ActivityQualification toEntity(ActivityQualificationUpdateDTO dto);

  ActivityQualification toEntity(ActivityQualificationUpdateStatusDTO dto);
}
