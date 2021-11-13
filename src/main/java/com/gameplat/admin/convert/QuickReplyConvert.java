package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.QuickReply;
import com.gameplat.admin.model.dto.QuickReplyDTO;
import com.gameplat.admin.model.vo.QuickReplyVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface QuickReplyConvert {

  @Mapping(source = "id", target = "quickId")
  QuickReply toEntity(QuickReplyDTO replyDTO);

  @Mapping(source = "quickId", target = "id")
  QuickReplyVO toVo(QuickReply reply);
}
