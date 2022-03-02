package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.ActivityLobbyDiscountDTO;
import com.gameplat.admin.model.vo.ActivityLobbyDiscountVO;
import com.gameplat.model.entity.activity.ActivityLobbyDiscount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ActivityLobbyDiscountConvert {

  ActivityLobbyDiscountVO toVO(ActivityLobbyDiscount activityLobbyDiscount);

  ActivityLobbyDiscount toEntity(ActivityLobbyDiscountDTO lobbyDiscountDTO);

  ActivityLobbyDiscount toEntity(ActivityLobbyDiscountVO activityLobbyDiscountVO);
}
