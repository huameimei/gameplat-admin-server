package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.ChatPushPlanAddOrEditDTO;
import com.gameplat.admin.model.vo.ChatPushPlanVO;
import com.gameplat.model.entity.chart.ChatPushPlan;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChatPushPlanConvert {

  ChatPushPlanVO toVo(ChatPushPlan entity);

  ChatPushPlan toEntity(ChatPushPlanAddOrEditDTO dto);
}
