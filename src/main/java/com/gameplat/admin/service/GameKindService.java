package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.GameKindQueryDTO;
import com.gameplat.admin.model.dto.OperGameKindDTO;
import com.gameplat.admin.model.vo.GameKindVO;
import com.gameplat.model.entity.game.GameKind;

import java.util.List;

public interface GameKindService extends IService<GameKind> {

  IPage<GameKindVO> selectGameKindList(PageDTO<GameKind> page, GameKindQueryDTO dto);

  void updateGameKind(OperGameKindDTO operGameKindDTO);

  void updateEnable(OperGameKindDTO operGameKindDTO);

  void updateDemoEnable(OperGameKindDTO operGameKindDTO);

  /** 根据游戏大类类型获取相关游戏平台 */
  List<GameKindVO> getGameKindInBanner(String gameType);

  GameKindVO getByCode(String code);

  List<GameKind> queryGameKindListByPlatformCode(String platformCode);
}
