package com.gameplat.admin.service.impl;

import com.gameplat.admin.config.WebSocketConfig;
import com.gameplat.admin.model.bean.PushMessage;
import com.gameplat.admin.service.MessagePushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MessagePushServiceImpl implements MessagePushService {

  @Autowired private SimpMessagingTemplate messagingTemplate;

  @Override
  @Async
  public void send(String account, PushMessage message) {
    messagingTemplate.convertAndSendToUser(account, WebSocketConfig.USER_BROKER, message);
  }

  @Override
  public void send(PushMessage message) {
    this.convertAndSend(WebSocketConfig.TOPIC_BROKER, message);
  }

  @Override
  public void convertAndSend(String destination, PushMessage message) {
    messagingTemplate.convertAndSend(destination, message);
  }
}
