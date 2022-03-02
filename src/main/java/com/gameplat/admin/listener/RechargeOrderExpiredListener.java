package com.gameplat.admin.listener;

import com.gameplat.admin.service.RechargeOrderService;
import com.gameplat.common.enums.RechargeStatus;
import com.gameplat.model.entity.recharge.RechargeOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.util.Date;

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
      RechargeOrder rechargeOrder =
          rechargeOrderService.getById(Long.valueOf(channel.split(channel)[1]));
      if (RechargeStatus.UNHANDLED.getValue() == rechargeOrder.getStatus()) {
        rechargeOrder.setStatus(RechargeStatus.CANCELLED.getValue());
        rechargeOrder.setAuditRemarks("自动取消");
        rechargeOrder.setAuditTime(new Date());
        rechargeOrder.setAuditorAccount("系统审批");
        rechargeOrder.setRemarks("自动取消");
        rechargeOrderService.updateById(rechargeOrder);
      }
    }
  }
}
