package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.GamePlatform;
import com.gameplat.admin.model.dto.GamePlatformQueryDTO;
import com.gameplat.admin.model.dto.OperGamePlatformDTO;

public interface GamePlatformService extends IService<GamePlatform> {

  IPage<GamePlatform> selectGamePlatformList(PageDTO<GamePlatform> page, GamePlatformQueryDTO dto);

  void updateGamePlatform(OperGamePlatformDTO operGamePlatformDTO);
}
