package com.gameplat.admin.consumer;

import com.gameplat.common.message.GamePlatformMessage;
import org.springframework.messaging.handler.annotation.Payload;

public interface GamePlatformConsumer<T> {
  /**
   * 消息消费
   *
   * @param message InstallPayPluginsMessage
   */
  void receive(@Payload GamePlatformMessage<T> message);
}
