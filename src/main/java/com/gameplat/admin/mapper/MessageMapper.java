package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.domain.MessageInfo;
import com.gameplat.admin.model.dto.MessageInfoAddDTO;

public interface MessageMapper extends BaseMapper<MessageInfo> {

    void saveReturnId(MessageInfo messageInfo);
}
