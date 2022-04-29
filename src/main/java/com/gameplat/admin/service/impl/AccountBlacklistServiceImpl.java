package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.AccountBlacklistConvert;
import com.gameplat.admin.mapper.AccountBlacklistMapper;
import com.gameplat.admin.model.dto.AccountBlacklistQueryDTO;
import com.gameplat.admin.model.dto.OperAccountBlacklistDTO;
import com.gameplat.admin.service.AccountBlacklistService;
import com.gameplat.admin.service.MemberService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.model.entity.blacklist.AccountBlacklist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class AccountBlacklistServiceImpl
    extends ServiceImpl<AccountBlacklistMapper, AccountBlacklist>
    implements AccountBlacklistService {

  @Autowired private AccountBlacklistConvert accountBlacklistConvert;

  @Autowired private MemberService memberService;

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
    if (StringUtils.isNotBlank(dto.getAccount())) {
      memberService
          .getByAccount(dto.getAccount())
          .orElseThrow(() -> new ServiceException("会员信息不存在！"));
    }

    if (StringUtils.isBlank(dto.getAccount()) && StringUtils.isBlank(dto.getIp())) {
      throw new ServiceException("会员帐号和IP地址不能同时为空");
    }

    AccountBlacklist accountBlacklist = accountBlacklistConvert.toEntity(dto);
    if (!this.updateById(accountBlacklist)) {
      throw new ServiceException("更新会员黑名单失败!");
    }
  }

  @Override
  public void save(OperAccountBlacklistDTO dto) {
    if (StringUtils.isNotBlank(dto.getAccount())) {
      memberService
          .getByAccount(dto.getAccount())
          .orElseThrow(() -> new ServiceException("会员信息不存在！"));
    }

    if (StringUtils.isBlank(dto.getAccount()) && StringUtils.isBlank(dto.getIp())) {
      throw new ServiceException("会员帐号和IP地址不能同时为空");
    }

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
