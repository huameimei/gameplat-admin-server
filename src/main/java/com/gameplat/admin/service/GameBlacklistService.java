package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.GameBlacklist;
import com.gameplat.admin.model.dto.GameBlacklistQueryDTO;
import com.gameplat.admin.model.dto.OperGameBlacklistDTO;
import java.util.List;

public interface GameBlacklistService extends IService<GameBlacklist> {

  IPage<GameBlacklist> queryGameBlacklistList(
      PageDTO<GameBlacklist> page, GameBlacklistQueryDTO dto);

  void update(OperGameBlacklistDTO dto);

  void save(OperGameBlacklistDTO dto);

  void delete(Long id);

  List<GameBlacklist> selectGameBlackList(GameBlacklist example);
}
