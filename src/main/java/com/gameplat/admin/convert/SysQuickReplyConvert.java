package com.gameplat.admin.convert;

import com.gameplat.admin.model.entity.SysQuickReply;
import com.gameplat.admin.model.vo.SysQuickReplyVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SysQuickReplyConvert {

  SysQuickReplyVo toVo(SysQuickReply sysQuickReply);
}
