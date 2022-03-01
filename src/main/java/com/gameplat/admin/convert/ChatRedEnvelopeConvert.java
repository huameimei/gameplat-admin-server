package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.ChatRedEnvelopeAddDTO;
import com.gameplat.admin.model.vo.ChatRedEnvelopeVO;
import com.gameplat.model.entity.chart.ChatRedEnvelope;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChatRedEnvelopeConvert {

  ChatRedEnvelope toEntity(ChatRedEnvelopeAddDTO dto);

  ChatRedEnvelopeVO toVo(ChatRedEnvelope entity);
}
