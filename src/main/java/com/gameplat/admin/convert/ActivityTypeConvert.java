package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.ActivityType;
import com.gameplat.admin.model.dto.ActivityTypeAddDTO;
import com.gameplat.admin.model.dto.ActivityTypeDTO;
import com.gameplat.admin.model.dto.ActivityTypeUpdateDTO;
import com.gameplat.admin.model.vo.ActivityTypeVO;
import org.mapstruct.Mapper;

/**
 * 活动类型转换
 *
 * @author admin
 */
@Mapper(componentModel = "spring")
public interface ActivityTypeConvert {

    /**
     * 将Entity转换成VO
     *
     * @param activityType
     * @return
     */
    ActivityTypeVO toVo(ActivityType activityType);


    /**
     * DTO转Entity
     *
     * @param activityTypeDTO
     * @return
     */
    ActivityType toEntity(ActivityTypeDTO activityTypeDTO);

    ActivityType toEntity(ActivityTypeAddDTO activityTypeAddDTO);

    ActivityType toEntity(ActivityTypeUpdateDTO activityTypeUpdateDTO);
}
