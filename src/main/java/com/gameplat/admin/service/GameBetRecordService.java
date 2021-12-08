package com.gameplat.admin.service;

import com.gameplat.common.message.GameBetRecordMessage;
import java.util.List;

/**
 * 游戏注单接口
 *
 * @author robben
 * @param <T>
 */
public interface GameBetRecordService<T> {

  /**
   * 处理
   *
   * @param message GameBetRecordMessage
   */
  default void process(GameBetRecordMessage<T> message) {
    // 计算打码量
    this.calcGameDml();

    // 保存注单
    this.saveRecords(message.getPayload());
  }

  /** 计算打码量 */
  void calcGameDml();

  /**
   * 保存注单记录
   *
   * @param records List
   */
  void saveRecords(List<T> records);
}
