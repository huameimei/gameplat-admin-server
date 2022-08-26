package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.common.game.GameBizBean;
import com.gameplat.model.entity.game.GameTransferInfo;

import java.util.List;

public interface GameTransferInfoService extends IService<GameTransferInfo> {

  List<GameTransferInfo> getAllGameTransferInfo();

  List<GameTransferInfo> getGameTransferInfoList(Long memberId);

  GameTransferInfo getGameTransferInfo(Long memberId, String platformCode);

  void insertOrUpdate(GameTransferInfo gameTransferInfo);

  void asyncUpdate(GameTransferInfo gameTransferInfo);

  void asyncQueryAndUpdate(GameTransferInfo gameTransferInfo, GameBizBean gameBizBean) throws Exception;
}
