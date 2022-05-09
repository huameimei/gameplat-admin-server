package com.gameplat.admin.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.BankBlacklistConvert;
import com.gameplat.admin.mapper.BankBlacklistMapper;
import com.gameplat.admin.model.dto.BankBlacklistQueryDTO;
import com.gameplat.admin.model.dto.OperBankBlacklistDTO;
import com.gameplat.admin.service.BankBlacklistService;
import com.gameplat.base.common.util.DateUtils;
import com.gameplat.common.lang.Assert;
import com.gameplat.model.entity.blacklist.BankBlacklist;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class BankBlacklistServiceImpl extends ServiceImpl<BankBlacklistMapper, BankBlacklist>
    implements BankBlacklistService {

  @Autowired private BankBlacklistConvert bankBlacklistConvert;

  @Autowired private BankBlacklistMapper bankBlacklistMapper;

  @Override
  public IPage<BankBlacklist> queryBankBlacklistList(
      PageDTO<BankBlacklist> page, BankBlacklistQueryDTO dto) {
    QueryWrapper<BankBlacklist> queryWrapper = Wrappers.query();
    queryWrapper.eq(ObjectUtils.isNotEmpty(dto.getCardNo()), "card_no", dto.getCardNo());
    if (StringUtils.isNotBlank(dto.getStartTime())) {
      queryWrapper.apply(
          "create_time >= STR_TO_DATE({0}, '%Y-%m-%d %H:%i:%s')",
          DateUtil.beginOfDay(DateUtils.parseDate(dto.getStartTime(), DateUtils.DATE_PATTERN)));
    }
    if (StringUtils.isNotBlank(dto.getEndTime())) {
      queryWrapper.apply(
          "create_time <= STR_TO_DATE({0}, '%Y-%m-%d %H:%i:%s')",
          DateUtil.endOfDay(DateUtils.parseDate(dto.getEndTime(), DateUtils.DATE_PATTERN)));
    }
    queryWrapper.orderByDesc("create_time");
    return bankBlacklistMapper.selectPage(page, queryWrapper);
  }

  @Override
  public void update(OperBankBlacklistDTO dto) {
    Assert.isTrue(this.updateById(bankBlacklistConvert.toEntity(dto)), "更新银行卡黑名单失败！");
  }

  @Override
  public void save(OperBankBlacklistDTO dto) {
    Assert.isTrue(this.save(bankBlacklistConvert.toEntity(dto)), "添加银行卡黑名单失败！");
  }

  @Override
  public void delete(Long id) {
    Assert.isTrue(this.removeById(id), "删除银行卡黑名单失败！");
  }

  @Override
  public BankBlacklist queryByCardNo(BankBlacklistQueryDTO dto) {
    return this.lambdaQuery()
        .eq(ObjectUtils.isNotEmpty(dto.getCardNo()), BankBlacklist::getCardNo, dto.getCardNo())
        .one();
  }
}
