package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.MessageInfo;
import com.gameplat.admin.model.dto.MessageInfoAddDTO;
import com.gameplat.admin.model.dto.MessageInfoEditDTO;
import com.gameplat.admin.model.vo.MessageInfoVO;
import org.mapstruct.Mapper;

/**
 * 个人消息转换器
 *
 * @author kenvin
 */
@Mapper(componentModel = "spring")
public interface MessageInfoConvert {

    MessageInfo toEntity(MessageInfoAddDTO messageInfoAddDTO);

    MessageInfo toEntity(MessageInfoEditDTO messageInfoEditDTO);

    MessageInfoVO toVo(MessageInfo messageInfo);

}
