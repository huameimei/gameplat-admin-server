package com.gameplat.admin.consumer.game;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.MessageChannel;

public interface GameSink {
  String INPUT = "gameInput";

  @Input(INPUT)
  MessageChannel input();
}
