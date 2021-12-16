package com.gameplat.admin.consumer.gameplatform;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gameplat.admin.consumer.GamePlatformConsumer;
import com.gameplat.admin.model.domain.GamePlatform;
import com.gameplat.admin.service.GamePlatformService;
import com.gameplat.common.message.GamePlatformMessage;
import java.util.Optional;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;

@Slf4j
@EnableBinding(GamePlatformSink.class)
public class GamePlatformConsumerImpl implements GamePlatformConsumer<Object> {

//  @Value("${spring.cloud.stream.bindings.gamePlatformInput.group}")
  private String gamePlatformConsumerGroup;

  @Resource
  private GamePlatformService gamePlatformService;

  @Override
  public void receive(GamePlatformMessage<Object> message) {
    log.info("GamePlatform真人游戏消费者，收到消息:{}", message);
    String[] split = gamePlatformConsumerGroup.split("@");
    String tenant = split[1];
    if (message.getTenant() != null && !message.getTenant().contains(tenant)) {
      return;
    }
    Object payload = message.getPayload();
    GamePlatform receiveInfo = JSONUtil.toBean(JSONUtil.toJsonStr(payload), GamePlatform.class);
    LambdaQueryWrapper<GamePlatform> queryWrapper = Wrappers.lambdaQuery();
    queryWrapper.eq(GamePlatform::getCode,receiveInfo.getCode());
    GamePlatform resp = Optional.ofNullable(gamePlatformService.getOne(queryWrapper)).orElse(null);
    if (resp != null) {
      gamePlatformService.update(receiveInfo,queryWrapper);
    } else {
      gamePlatformService.save(receiveInfo);
    }
  }
}
