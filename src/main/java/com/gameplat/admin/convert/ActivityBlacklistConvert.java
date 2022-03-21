package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.ActivityBlacklistAddDTO;
import com.gameplat.admin.model.vo.ActivityBlacklistVO;
import com.gameplat.model.entity.activity.ActivityBlacklist;
import org.mapstruct.Mapper;

/**
 * 活动黑名单转换器
 *
 * @author kenvin
 */
@Mapper(componentModel = "spring")
public interface ActivityBlacklistConvert {

  ActivityBlacklistVO toVo(ActivityBlacklist activityBlacklist);

  ActivityBlacklist toEntity(ActivityBlacklistAddDTO dto);
}
