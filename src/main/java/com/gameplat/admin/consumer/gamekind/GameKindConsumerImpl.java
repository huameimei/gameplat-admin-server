package com.gameplat.admin.consumer.gamekind;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gameplat.admin.consumer.GameKindConsumer;
import com.gameplat.admin.consumer.gameplatform.GamePlatformSink;
import com.gameplat.admin.model.domain.GameKind;
import com.gameplat.admin.service.GameKindService;
import com.gameplat.common.message.GameKindMessage;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;

@Slf4j
@EnableBinding(GamePlatformSink.class)
public class GameKindConsumerImpl implements GameKindConsumer {

//  @Value("${spring.cloud.stream.bindings.gameKindInput.group}")
  private String gameKindConsumerGroup;

  @Resource
  private GameKindService gameKindService;

  @Override
  public void receive(GameKindMessage message) {
    log.info("GameKind真人游戏类别消费者，收到消息:{}", message);
    String[] split = gameKindConsumerGroup.split("@");
    String tenant = split[1];
    if (message.getTenant() != null && !message.getTenant().contains(tenant)) {
      return;
    }
    Object payload = message.getPayload();
    List<GameKind> receiveList = JSONUtil.toList(JSONUtil.toJsonStr(payload),GameKind.class);
    if(CollectionUtil.isNotEmpty(receiveList)){
      receiveList.forEach(item -> {
        LambdaQueryWrapper<GameKind> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(GameKind::getCode,item.getCode());
        GameKind gameKind = gameKindService.getOne(queryWrapper);
        if (ObjectUtil.isNotNull(gameKind)){
          gameKindService.update(item,queryWrapper);
        }else{
          gameKindService.save(item);
        }
      });
    }
  }
}
