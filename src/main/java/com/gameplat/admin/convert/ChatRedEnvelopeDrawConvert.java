package com.gameplat.admin.convert;

import com.gameplat.admin.model.vo.ChatRedEnvelopeDrawVO;
import com.gameplat.model.entity.chart.ChatRedEnvelopeDraw;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChatRedEnvelopeDrawConvert {

  ChatRedEnvelopeDrawVO toVo(ChatRedEnvelopeDraw entity);
}
