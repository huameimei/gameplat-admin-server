package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.MessageDistributeMapper;
import com.gameplat.admin.service.MessageDistributeService;
import com.gameplat.model.entity.message.MessageDistribute;
import org.springframework.stereotype.Service;

/**
 * @author kenvin
 */
@Service
public class MessageDistributeServiceImpl
    extends ServiceImpl<MessageDistributeMapper, MessageDistribute>
    implements MessageDistributeService {}
