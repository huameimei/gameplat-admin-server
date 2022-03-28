package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.ActivityInfoAddDTO;
import com.gameplat.admin.model.dto.ActivityInfoDTO;
import com.gameplat.admin.model.dto.ActivityInfoUpdateDTO;
import com.gameplat.admin.model.vo.ActivityInfoVO;
import com.gameplat.model.entity.activity.ActivityInfo;
import org.mapstruct.Mapper;

/**
 * 活动信息转换器
 *
 * @author kenvin
 */
@Mapper(componentModel = "spring")
public interface ActivityInfoConvert {

  ActivityInfoVO toVo(ActivityInfo entity);

  ActivityInfo toEntity(ActivityInfoDTO dto);

  ActivityInfo toEntity(ActivityInfoAddDTO dto);

  ActivityInfo toEntity(ActivityInfoUpdateDTO dto);
}
