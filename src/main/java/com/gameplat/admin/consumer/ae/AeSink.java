package com.gameplat.admin.consumer.ae;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.MessageChannel;

public interface AeSink {

  String INPUT = "aeInput";

  @Input(INPUT)
  MessageChannel input();
}
