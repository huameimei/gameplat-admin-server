package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.ChatNotice;
import com.gameplat.admin.model.dto.ChatNoticeAddDTO;
import com.gameplat.admin.model.dto.ChatNoticeEditDTO;
import com.gameplat.admin.model.vo.ChatNoticeVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChatNoticeConvert {

  ChatNoticeVO toVo (ChatNotice entity);

  ChatNotice toEntity (ChatNoticeAddDTO dto);

  ChatNotice toEntity (ChatNoticeEditDTO dto);
}
