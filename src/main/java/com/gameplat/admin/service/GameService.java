package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.Game;
import com.gameplat.admin.model.dto.GameQueryDTO;
import com.gameplat.admin.model.dto.OperGameDTO;
import com.gameplat.admin.model.vo.GameVO;

import java.util.List;

public interface GameService extends IService<Game> {

  IPage<Game> selectGameList(PageDTO<Game> page, GameQueryDTO dto);

  void updateGame(OperGameDTO operGameDTO);

  /**
   * 通过游戏类型查询游戏列表
   * @param gameTypeCode
   * @return
   */
  List<GameVO> findByGameTypeCode(String gameTypeCode);
}
