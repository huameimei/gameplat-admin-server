package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.AccountBlacklist;
import com.gameplat.admin.model.dto.AccountBlacklistQueryDTO;
import com.gameplat.admin.model.dto.OperAccountBlacklistDTO;

public interface AccountBlacklistService extends IService<AccountBlacklist> {

  IPage<AccountBlacklist> selectAccountBlacklistList(
      PageDTO<AccountBlacklist> page, AccountBlacklistQueryDTO dto);

  void update(OperAccountBlacklistDTO dto);

  void save(OperAccountBlacklistDTO dto);

  void delete(Long id);
}
