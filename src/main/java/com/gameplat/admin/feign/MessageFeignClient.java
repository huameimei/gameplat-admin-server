package com.gameplat.admin.feign;

import com.gameplat.common.game.config.FeignRestConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(value = "web-server",configuration = FeignRestConfig.class)
//@FeignClient(value = "OLDMAN-web-server", configuration = FeignRestConfig.class)
public interface MessageFeignClient {

    /**
     * 点对点消息socket消息推送
     */
    @Async
    @PostMapping("/api/internal/msg/user")
    void userSend(@RequestBody Map<String, String> map);

    /**
     * 广播模式socket消息推送
     */
    @Async
    @PostMapping(value = "/api/internal/msg/topic", consumes = MediaType.APPLICATION_JSON_VALUE)
    void topicSend(@RequestBody Map<String, String> map);
}
