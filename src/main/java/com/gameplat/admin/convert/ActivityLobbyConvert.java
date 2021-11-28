package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.ActivityLobby;
import com.gameplat.admin.model.dto.ActivityLobbyAddDTO;
import com.gameplat.admin.model.dto.ActivityLobbyUpdateDTO;
import com.gameplat.admin.model.dto.ActivityLobbyUpdateStatusDTO;
import com.gameplat.admin.model.vo.ActivityLobbyVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ActivityLobbyConvert {


    ActivityLobbyVO toVo(ActivityLobby activityLobby);

    ActivityLobby toEntity(ActivityLobbyAddDTO activityLobbyAddDTO);

    ActivityLobby toEntity(ActivityLobbyUpdateDTO activityLobbyUpdateDTO);

    ActivityLobby toEntity(ActivityLobbyUpdateStatusDTO activityLobbyUpdateStatusDTO);
}
