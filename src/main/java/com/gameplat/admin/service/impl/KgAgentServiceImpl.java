package com.gameplat.admin.service.impl;

import com.gameplat.admin.service.GameConfigService;
import com.gameplat.admin.service.KgAgentService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.common.enums.GamePlatformEnum;
import com.gameplat.common.enums.TransferTypesEnum;
import com.gameplat.common.game.GameBizBean;
import com.gameplat.common.game.api.GameApi;
import com.gameplat.model.entity.member.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @description:
 * @author: francis
 * @create: 2022-07-26 21:12
 **/
public class KgAgentServiceImpl implements KgAgentService {

  private static final String backslash = "/";

  @Autowired private ApplicationContext applicationContext;

  @Autowired private GameConfigService gameConfigService;

  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public GameApi getGameApi(String platformCode) {
    GameApi api =
      applicationContext.getBean(platformCode.toLowerCase() + GameApi.SUFFIX, GameApi.class);
    TransferTypesEnum tt = TransferTypesEnum.get(platformCode);
    // 1代表是否额度转换
    if (tt == null || tt.getType() != 1) {
      throw new ServiceException("游戏未接入");
    }
    return api;
  }

  @Async
  @Override
  public void changeKgLotteryProxy(Member source, Member target) {
    String superPath = target.getSuperPath().concat(source.getAccount()).concat(backslash);
    GameApi gameApi = getGameApi(GamePlatformEnum.KGNL.getCode());
    GameBizBean gameBizBean = GameBizBean.builder()
      .account(source.getAccount())
      .platformCode(GamePlatformEnum.KGNL.getCode())
      .superPath(superPath)
      .config(gameConfigService.getGameConfig(GamePlatformEnum.KGNL.getCode())).build();
    gameApi.changeGameProxy(gameBizBean);
  }

}
