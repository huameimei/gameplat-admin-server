package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.domain.Message;
import com.gameplat.admin.model.dto.MessageAddDTO;

public interface MessageMapper extends BaseMapper<Message> {

    void saveReturnId(MessageAddDTO dto);
}
