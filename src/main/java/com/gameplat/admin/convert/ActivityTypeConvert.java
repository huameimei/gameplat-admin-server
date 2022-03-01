package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.ActivityTypeAddDTO;
import com.gameplat.admin.model.dto.ActivityTypeUpdateDTO;
import com.gameplat.admin.model.vo.ActivityTypeVO;
import com.gameplat.model.entity.activity.ActivityType;
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
   * @param entity ActivityType
   * @return
   */
  ActivityTypeVO toVo(ActivityType entity);

  ActivityType toEntity(ActivityTypeAddDTO dto);

  ActivityType toEntity(ActivityTypeUpdateDTO dto);
}
