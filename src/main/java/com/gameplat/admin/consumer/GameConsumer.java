package com.gameplat.admin.consumer;

import com.gameplat.common.message.GameMessage;
import org.springframework.messaging.handler.annotation.Payload;

public interface GameConsumer<T> {
  /**
   * 消息消费
   */
  void receive(@Payload GameMessage<T> message);
}
