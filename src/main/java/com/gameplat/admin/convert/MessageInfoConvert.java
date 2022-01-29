package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.MessageInfo;
import com.gameplat.admin.model.dto.MessageInfoAddDTO;
import com.gameplat.admin.model.dto.MessageInfoEditDTO;
import com.gameplat.admin.model.vo.MessageInfoVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * 个人消息转换器
 *
 * @author kenvin
 */
@Mapper(componentModel = "spring")
public interface MessageInfoConvert {

    @Mapping(target = "beginTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "endTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    MessageInfo toEntity(MessageInfoAddDTO messageInfoAddDTO);

    @Mapping(target = "beginTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "endTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    MessageInfo toEntity(MessageInfoEditDTO messageInfoEditDTO);

    MessageInfoVO toVo(MessageInfo messageInfo);

}
