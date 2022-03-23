package com.gameplat.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.SalaryGrantConvert;
import com.gameplat.admin.mapper.SalaryGrantMapper;
import com.gameplat.admin.model.dto.SalaryGrantDTO;
import com.gameplat.admin.model.vo.SalaryGrantVO;
import com.gameplat.admin.service.SalaryGrantService;
import com.gameplat.model.entity.proxy.SalaryGrant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class SalaryGrantServiceImpl extends ServiceImpl<SalaryGrantMapper, SalaryGrant>
    implements SalaryGrantService {

  @Autowired private SalaryGrantMapper salaryGrantMapper;

  @Autowired private SalaryGrantConvert salaryGrantConvert;

  @Override
  public IPage<SalaryGrantVO> queryPage(PageDTO<SalaryGrant> page, SalaryGrantDTO dto) {
    LambdaQueryWrapper<SalaryGrant> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper
        .eq(
            ObjectUtils.isNotNull(dto.getPeriodsId()),
            SalaryGrant::getPeriodsId,
            dto.getPeriodsId())
        .eq(StrUtil.isNotBlank(dto.getAccount()), SalaryGrant::getAccount, dto.getAccount())
        .eq(StrUtil.isNotBlank(dto.getGameType()), SalaryGrant::getGameType, dto.getGameType())
        .eq(
            ObjectUtils.isNotNull(dto.getAgentLevel()),
            SalaryGrant::getAgentLevel,
            dto.getAgentLevel())
        .eq(
            ObjectUtils.isNotNull(dto.getReachStatus()),
            SalaryGrant::getReachStatus,
            dto.getReachStatus())
        .eq(
            ObjectUtils.isNotNull(dto.getGrantStatus()),
            SalaryGrant::getGrantStatus,
            dto.getGrantStatus());
    queryWrapper.orderByDesc(SalaryGrant::getCreateTime);
    return salaryGrantMapper.selectPage(page, queryWrapper).convert(salaryGrantConvert::toVo);
  }

  @Override
  public void change(Long id, BigDecimal salaryAmount) {
    Assert.isTrue(id != null, "主键参数不能为空！");
    Assert.isTrue(salaryAmount.compareTo(BigDecimal.ZERO) >= 0, "修改后工资额不能小于0！");
    SalaryGrant salaryGrant = SalaryGrant.builder().id(id).salaryAmount(salaryAmount).build();
    log.info("调整工资额--{}--{}", id, salaryAmount);
    Assert.isTrue(this.updateById(salaryGrant), "调整失败！");
  }
}
