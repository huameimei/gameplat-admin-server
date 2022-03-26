package com.gameplat.admin.listener;

import com.gameplat.admin.service.RechargeOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Slf4j
public class RechargeOrderExpiredListener extends KeyExpirationEventMessageListener {

  @Autowired private RechargeOrderService rechargeOrderService;

  public RechargeOrderExpiredListener(RedisMessageListenerContainer listenerContainer) {
    super(listenerContainer);
  }

  @Override
  public void onMessage(Message message, byte[] pattern) {
    String channel = message.toString();
    if (channel.startsWith("rechargeOrder") && channel.split(channel).length > 1) {
      rechargeOrderService.expired(Long.valueOf(channel.split(channel)[1]));
    }
  }
}
