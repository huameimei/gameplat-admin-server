package com.gameplat.admin.feign;

import com.gameplat.common.game.config.FeignRestConfig;
import com.gameplat.message.model.MessagePayload;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "web-server", configuration = FeignRestConfig.class)
// @FeignClient(value = "OLDMAN-web-server", configuration = FeignRestConfig.class)
public interface MessageFeignClient {

  /** 点对点消息socket消息推送 */
  @Async
  @PostMapping("/api/internal/msg/sendToUser/{username}")
  void sendToUser(@PathVariable("username") String username, @RequestBody MessagePayload payload);

  /** 广播模式socket消息推送 */
  @Async
  @PostMapping(value = "/api/internal/msg/sendAll", consumes = MediaType.APPLICATION_JSON_VALUE)
  void send(@RequestBody MessagePayload payload);
}
