package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.ChatRedEnvelopeMapper;
import com.gameplat.admin.model.domain.ChatRedEnvelope;
import com.gameplat.admin.service.ChatRedEnvelopeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lily
 * @description 聊天室紅包管理
 * @date 2022/2/14
 */

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class ChatRedEnvelopeServiceImpl extends ServiceImpl<ChatRedEnvelopeMapper, ChatRedEnvelope> implements ChatRedEnvelopeService {
}
