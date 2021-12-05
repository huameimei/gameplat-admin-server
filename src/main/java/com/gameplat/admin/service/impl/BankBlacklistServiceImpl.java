package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.BankBlacklistConvert;
import com.gameplat.admin.mapper.BankBlacklistMapper;
import com.gameplat.admin.model.domain.BankBlacklist;
import com.gameplat.admin.model.dto.BankBlacklistQueryDTO;
import com.gameplat.admin.model.dto.OperBankBlacklistDTO;
import com.gameplat.admin.service.BankBlacklistService;
import com.gameplat.base.common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class BankBlacklistServiceImpl extends ServiceImpl<BankBlacklistMapper, BankBlacklist>
    implements BankBlacklistService {

  @Autowired private BankBlacklistConvert bankBlacklistConvert;

  @Override
  public IPage<BankBlacklist> queryBankBlacklistList(
      PageDTO<BankBlacklist> page, BankBlacklistQueryDTO dto) {
    return this.lambdaQuery()
        .eq(ObjectUtils.isNotEmpty(dto.getCardNo()), BankBlacklist::getCardNo, dto.getCardNo())
        .page(page);
  }

  @Override
  public void update(OperBankBlacklistDTO dto) {
    BankBlacklist bankBlacklist = bankBlacklistConvert.toEntity(dto);
    if (!this.updateById(bankBlacklist)) {
      throw new ServiceException("更新银行卡黑名单失败！");
    }
  }

  @Override
  public void save(OperBankBlacklistDTO dto) {
    BankBlacklist bankBlacklist = bankBlacklistConvert.toEntity(dto);
    if (!this.save(bankBlacklist)) {
      throw new ServiceException("添加银行卡黑名单失败！");
    }
  }

  @Override
  public void delete(Long id) {
    if (!this.removeById(id)) {
      throw new ServiceException("删除银行卡黑名单失败！");
    }
  }

  @Override
  public BankBlacklist queryByCardNo(BankBlacklistQueryDTO dto) {
    return this.lambdaQuery()
        .eq(ObjectUtils.isNotEmpty(dto.getCardNo()), BankBlacklist::getCardNo, dto.getCardNo())
        .one();
  }
}
