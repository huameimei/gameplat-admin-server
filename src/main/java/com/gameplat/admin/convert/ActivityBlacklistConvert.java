package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.ActivityBlacklist;
import com.gameplat.admin.model.dto.ActivityBlacklistAddDTO;
import com.gameplat.admin.model.dto.ActivityBlacklistDTO;
import com.gameplat.admin.model.vo.ActivityBlacklistVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ActivityBlacklistConvert {

    ActivityBlacklist toEntity(ActivityBlacklistDTO activityBlacklistDTO);

    ActivityBlacklistVO toVo(ActivityBlacklist activityBlacklist);

    ActivityBlacklist toEntity(ActivityBlacklistAddDTO activityBlacklistAddDTO);

}
