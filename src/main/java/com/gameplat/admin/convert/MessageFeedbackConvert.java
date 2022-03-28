package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.MessageFeedbackAddDTO;
import com.gameplat.admin.model.dto.MessageFeedbackUpdateDTO;
import com.gameplat.admin.model.vo.MessageFeedbackVO;
import com.gameplat.model.entity.message.MessageFeedback;
import org.mapstruct.Mapper;

/**
 * @author Lily
 */
@Mapper(componentModel = "spring")
public interface MessageFeedbackConvert {

  MessageFeedback toEntity(MessageFeedbackAddDTO dto);

  MessageFeedback toEntity(MessageFeedbackUpdateDTO dto);

  MessageFeedbackVO toVo(MessageFeedback messageInfo);
}
