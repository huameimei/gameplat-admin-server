package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.ChatRedEnvelope;
import com.gameplat.admin.model.dto.ChatRedEnvelopeAddDTO;
import com.gameplat.admin.model.vo.ChatRedEnvelopeVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChatRedEnvelopeConvert {

    ChatRedEnvelope toEntity(ChatRedEnvelopeAddDTO dto);

    ChatRedEnvelopeVO toVo(ChatRedEnvelope entity);
}
