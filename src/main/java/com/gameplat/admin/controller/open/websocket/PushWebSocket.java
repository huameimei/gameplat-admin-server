package com.gameplat.admin.controller.open.websocket;

import com.gameplat.web.websocket.AbstractWsSessionManager;
import com.gameplat.web.websocket.SessionChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * @Description: @Author: Hoover @Date: 2022/2/7 15:18
 */
@Slf4j
@Component
@ServerEndpoint(value = "/ws/adminPush/{userName}")
public class PushWebSocket extends AbstractWsSessionManager {

  private Session session;

  /** 连接建立 */
  @OnOpen
  public void onOpen(Session session, @PathParam("userName") String userName) {
    this.session = session;
    SessionChannel channel = new SessionChannel();
    channel.setSession(session);
    super.storeChannel(channel);
    try {
      log.info("-------连接建立");
      sendMessage("连接成功");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /** 连接关闭 */
  @OnClose
  public void onClose(Session session, @PathParam("userName") String userName) {
    log.info("-------连接关闭");
  }

  /** 收到消息 */
  @OnMessage
  public void onMessage(String message, Session session, @PathParam("userName") String userName) {
    try {
      log.info("-------客户端消息" + message);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /** 连接异常 */
  @OnError
  public void onError(Session session, Throwable error) {
    System.out.println("-------连接异常");
    error.printStackTrace();
  }

  /** 发送消息 */
  public void sendMessage(String message) throws IOException {
    this.session.getBasicRemote().sendText(message);
  }

  @Override
  public void send(SessionChannel sessionChannel, Object msg) {}
}
