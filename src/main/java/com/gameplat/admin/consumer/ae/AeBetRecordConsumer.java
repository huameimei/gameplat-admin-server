package com.gameplat.admin.consumer.ae;

import com.gameplat.admin.consumer.GameBetRecordConsumer;
import com.gameplat.admin.model.domain.AeBetRecord;
import com.gameplat.admin.service.GameBetRecordService;
import com.gameplat.common.message.GameBetRecordMessage;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;

/**
 * AE注单消费
 *
 * @author robben
 */
@Slf4j
@EnableBinding(AeSink.class)
public class AeBetRecordConsumer implements GameBetRecordConsumer<AeBetRecord> {

  @Resource(name = "aeBetRecordService")
  private GameBetRecordService<AeBetRecord> gameBetRecordService;

  @Override
  @StreamListener(AeSink.INPUT)
  public void receive(@Payload GameBetRecordMessage<AeBetRecord> message) {
    log.info("AE注单消费者，收到消息:{}", message);
    gameBetRecordService.process(message);
  }
}
