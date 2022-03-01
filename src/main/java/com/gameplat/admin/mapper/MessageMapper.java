package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.model.entity.message.Message;

public interface MessageMapper extends BaseMapper<Message> {

  void saveReturnId(Message message);
}
