package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.AccountBlacklistConvert;
import com.gameplat.admin.mapper.AccountBlacklistMapper;
import com.gameplat.admin.model.domain.AccountBlacklist;
import com.gameplat.admin.model.dto.AccountBlacklistQueryDTO;
import com.gameplat.admin.model.dto.OperAccountBlacklistDTO;
import com.gameplat.admin.service.AccountBlacklistService;
import com.gameplat.common.base.UserCredential;
import com.gameplat.common.exception.ServiceException;
import com.gameplat.security.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AccountBlacklistServiceImpl
    extends ServiceImpl<AccountBlacklistMapper, AccountBlacklist>
    implements AccountBlacklistService {

  @Autowired private AccountBlacklistConvert accountBlacklistConvert;

  @Override
  public IPage<AccountBlacklist> selectAccountBlacklistList(
      PageDTO<AccountBlacklist> page, AccountBlacklistQueryDTO dto) {
    return this.lambdaQuery()
        .eq(ObjectUtils.isNotEmpty(dto.getIp()), AccountBlacklist::getIp, dto.getIp())
        .eq(
            ObjectUtils.isNotEmpty(dto.getAccount()),
            AccountBlacklist::getAccount,
            dto.getAccount())
        .page(page);
  }

  @Override
  public void update(OperAccountBlacklistDTO dto) {
    AccountBlacklist accountBlacklist = accountBlacklistConvert.toEntity(dto);
    if (!this.updateById(accountBlacklist)) {
      throw new ServiceException("更新会员黑名单失败!");
    }
  }

  @Override
  public void save(OperAccountBlacklistDTO dto) {
    AccountBlacklist accountBlacklist = accountBlacklistConvert.toEntity(dto);
    if (!this.save(accountBlacklist)) {
      throw new ServiceException("新增会员黑名单失败!");
    }
  }

  @Override
  public void delete(Long id) {
    if (!this.removeById(id)) {
      throw new ServiceException("删除会员黑名单失败!");
    }
  }
}
