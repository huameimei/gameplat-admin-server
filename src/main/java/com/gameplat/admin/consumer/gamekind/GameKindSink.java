package com.gameplat.admin.consumer.gamekind;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.MessageChannel;

public interface GameKindSink {
  String INPUT = "gameKindInput";

  @Input(INPUT)
  MessageChannel input();
}
