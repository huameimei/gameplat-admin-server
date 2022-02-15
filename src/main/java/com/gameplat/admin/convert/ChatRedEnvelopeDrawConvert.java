package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.ChatRedEnvelopeDraw;
import com.gameplat.admin.model.vo.ChatRedEnvelopeDrawVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChatRedEnvelopeDrawConvert {

    ChatRedEnvelopeDrawVO toVo(ChatRedEnvelopeDraw entity);
}
