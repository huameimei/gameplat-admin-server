package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.BizBlacklist;
import com.gameplat.admin.model.domain.Member;
import com.gameplat.admin.model.dto.BizBlacklistQueryDTO;
import com.gameplat.admin.model.dto.OperBizBlacklistDTO;
import com.gameplat.common.exception.ServiceException;

import java.util.Set;

public interface BizBlacklistService extends IService<BizBlacklist> {

  IPage<BizBlacklist> queryBizBlacklistList(PageDTO<BizBlacklist> page, BizBlacklistQueryDTO dto);

  void update(OperBizBlacklistDTO dto);

  void save(OperBizBlacklistDTO dto);

  void delete(Long id);

  Set<String> getBizBlacklistTypesByUserId(Long userId) throws ServiceException;

  Set<String> getBizBlacklistTypesByUser(Member member) throws ServiceException;
}
