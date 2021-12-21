package com.gameplat.admin.consumer.game;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gameplat.admin.consumer.GameConsumer;
import com.gameplat.admin.consumer.GameKindConsumer;
import com.gameplat.admin.consumer.gameplatform.GamePlatformSink;
import com.gameplat.admin.model.domain.Game;
import com.gameplat.admin.model.domain.GameKind;
import com.gameplat.admin.service.GameKindService;
import com.gameplat.admin.service.GameService;
import com.gameplat.common.message.GameKindMessage;
import com.gameplat.common.message.GameMessage;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;

@Slf4j
@EnableBinding(GamePlatformSink.class)
public class GameConsumerImpl implements GameConsumer {

//  @Value("${spring.cloud.stream.bindings.gameInput.group}")
  private String gameConsumerGroup;

  @Resource
  private GameService gameService;

  @Override
  public void receive(GameMessage message) {
    log.info("Game真人游戏类别消费者，收到消息:{}", message);
    String[] split = gameConsumerGroup.split("@");
    String tenant = split[1];
    if (message.getTenant() != null && !message.getTenant().contains(tenant)) {
      return;
    }
    Object payload = message.getPayload();
    List<Game> receiveList = JSONUtil.toList(JSONUtil.toJsonStr(payload),Game.class);
    if(CollectionUtil.isNotEmpty(receiveList)){
      receiveList.forEach(item -> {
        LambdaQueryWrapper<Game> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Game::getGameCode,item.getGameCode());
        Game gameKind = gameService.getOne(queryWrapper);
        if (ObjectUtil.isNotNull(gameKind)){
          gameService.update(item,queryWrapper);
        }else{
          gameService.save(item);
        }
      });
    }
  }
}
