package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.PushMessage;
import com.gameplat.admin.model.dto.PushMessageAddDTO;
import com.gameplat.admin.model.dto.PushMessageDTO;
import com.gameplat.admin.model.dto.PushMessageEditDTO;
import com.gameplat.admin.model.vo.PushMessageVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PushMessageConvert {

    PushMessageVO toVo(PushMessage entity);

    PushMessage toEntity(PushMessageDTO pushMessageDTO);

    PushMessage toEntity(PushMessageAddDTO pushMessageDTO);

    PushMessage toEntity(PushMessageEditDTO pushMessageDTO);

}
