package com.gameplat.admin.consumer;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.MessageChannel;

public interface LiveConfigSink {
  String INPUT = "liveConfigInput";

  @Input(INPUT)
  MessageChannel input();
}
