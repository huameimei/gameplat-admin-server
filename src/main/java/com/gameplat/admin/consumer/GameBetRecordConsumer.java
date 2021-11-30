package com.gameplat.admin.consumer;

import com.gameplat.common.message.GameBetRecordMessage;
import org.springframework.messaging.handler.annotation.Payload;

/**
 * 游戏消费接口
 *
 * @author robben
 */
public interface GameBetRecordConsumer<T> {

  /**
   * 消息消费
   *
   * @param message GameBetRecordMessage
   */
  void receive(@Payload GameBetRecordMessage<T> message);
}
