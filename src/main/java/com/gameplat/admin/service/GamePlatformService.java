package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.GamePlatformQueryDTO;
import com.gameplat.admin.model.dto.OperGamePlatformDTO;
import com.gameplat.model.entity.game.GamePlatform;

import java.util.List;

public interface GamePlatformService extends IService<GamePlatform> {

  IPage<GamePlatform> selectGamePlatformList(PageDTO<GamePlatform> page, GamePlatformQueryDTO dto);

  void updateGamePlatform(OperGamePlatformDTO operGamePlatformDTO);

  List<GamePlatform> queryByTransfer();
}
