package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.Message;
import com.gameplat.admin.model.dto.MessageAddDTO;
import com.gameplat.admin.model.dto.MessageEditDTO;
import com.gameplat.admin.model.vo.MessageVO;
import org.mapstruct.Mapper;

/**
 * 个人消息转换器
 *
 * @author kenvin
 */
@Mapper(componentModel = "spring")
public interface MessageConvert {

    Message toEntity(MessageAddDTO messageAddDTO);

    Message toEntity(MessageEditDTO messageEditDTO);

    MessageVO toVo(Message message);


}
