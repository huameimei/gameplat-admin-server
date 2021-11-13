package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.BankBlacklist;
import com.gameplat.admin.model.dto.BankBlacklistQueryDTO;
import com.gameplat.admin.model.dto.OperBankBlacklistDTO;

public interface BankBlacklistService extends IService<BankBlacklist> {

  IPage<BankBlacklist> queryBankBlacklistList(
      PageDTO<BankBlacklist> page, BankBlacklistQueryDTO dto);

  void update(OperBankBlacklistDTO dto);

  void save(OperBankBlacklistDTO dto);

  void delete(Long id);

  BankBlacklist queryByCardNo(BankBlacklistQueryDTO dto);
}
