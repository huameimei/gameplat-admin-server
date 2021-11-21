package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.LiveBlacklist;
import com.gameplat.admin.model.dto.LiveBlacklistQueryDTO;
import com.gameplat.admin.model.dto.OperLiveBlacklistDTO;
import java.util.List;

public interface LiveBlacklistService extends IService<LiveBlacklist> {

  IPage<LiveBlacklist> queryLiveBlacklistList(
      PageDTO<LiveBlacklist> page, LiveBlacklistQueryDTO dto);

  void update(OperLiveBlacklistDTO dto);

  void save(OperLiveBlacklistDTO dto);

  void delete(Long id);

  List<LiveBlacklist> selectLiveBlackList(LiveBlacklist example);
}
