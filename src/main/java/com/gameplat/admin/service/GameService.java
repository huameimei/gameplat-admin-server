package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.Game;
import com.gameplat.admin.model.dto.GameQueryDTO;
import com.gameplat.admin.model.dto.OperGameDTO;

public interface GameService extends IService<Game> {

  IPage<Game> selectGameList(PageDTO<Game> page, GameQueryDTO dto);

  void updateGame(OperGameDTO operGameDTO);
}
