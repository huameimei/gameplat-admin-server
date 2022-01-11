package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.ActivityInfo;
import com.gameplat.admin.model.dto.ActivityInfoAddDTO;
import com.gameplat.admin.model.dto.ActivityInfoDTO;
import com.gameplat.admin.model.dto.ActivityInfoUpdateDTO;
import com.gameplat.admin.model.vo.ActivityInfoVO;
import org.mapstruct.Mapper;

/**
 * 活动信息转换器
 *
 * @author kenvin
 */
@Mapper(componentModel = "spring")
public interface ActivityInfoConvert {

  ActivityInfoVO toVo(ActivityInfo activityInfo);

  ActivityInfo toEntity(ActivityInfoDTO activityInfoDTO);

  ActivityInfo toEntity(ActivityInfoAddDTO activityInfoAddDTO);

  ActivityInfo toEntity(ActivityInfoUpdateDTO activityInfoUpdateDTO);
}
