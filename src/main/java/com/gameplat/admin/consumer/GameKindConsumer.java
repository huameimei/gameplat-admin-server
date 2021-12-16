package com.gameplat.admin.consumer;

import com.gameplat.common.message.GameKindMessage;
import org.springframework.messaging.handler.annotation.Payload;

public interface GameKindConsumer<T> {
  /**
   * 消息消费
   */
  void receive(@Payload GameKindMessage<T> message);
}
