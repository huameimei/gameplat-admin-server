package com.gameplat.admin.consumer.gameplatform;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.MessageChannel;

public interface GamePlatformSink {
  String INPUT = "gamePlatformInput";

  @Input(INPUT)
  MessageChannel input();
}
