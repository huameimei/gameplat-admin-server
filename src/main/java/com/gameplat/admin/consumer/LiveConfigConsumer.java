package com.gameplat.admin.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;

@Slf4j
@EnableBinding(LiveConfigSink.class)
public class LiveConfigConsumer {

  @StreamListener(LiveConfigSink.INPUT)
  public void receive(@Payload  String message) {
    log.info("真人配置数据，收到消息:{}", message);

  }
}
