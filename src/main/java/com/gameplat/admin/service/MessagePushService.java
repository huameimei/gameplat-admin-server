package com.gameplat.admin.service;


import com.gameplat.admin.model.bean.PushMessage;

public interface MessagePushService {

  /**
   * 私信
   *
   * @param account String
   * @param message PushMessage
   */
  void send(String account, PushMessage message);

  /**
   * 公共频道
   *
   * @param message PushMessage
   */
  void send(PushMessage message);

  /**
   * 发送至指定频道
   *
   * @param destination String
   * @param message PushMessage
   */
  void convertAndSend(String destination, PushMessage message);
}
