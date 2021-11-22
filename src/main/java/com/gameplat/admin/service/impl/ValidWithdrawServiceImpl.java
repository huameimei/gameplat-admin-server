package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.ValidWithdrawMapper;
import com.gameplat.admin.model.domain.RechargeOrder;
import com.gameplat.admin.model.domain.ValidWithdraw;
import com.gameplat.admin.service.ValidWithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class ValidWithdrawServiceImpl extends
    ServiceImpl<ValidWithdrawMapper, ValidWithdraw> implements
    ValidWithdrawService {

  @Autowired
  private ValidWithdrawMapper validWithdrawMapper;

  @Override
  public void addRechargeOrder(RechargeOrder rechargeOrder) throws Exception {
    ValidWithdraw validWithdraw = new ValidWithdraw();
    validWithdraw.setMemberId(rechargeOrder.getMemberId());
    validWithdraw.setAccount(rechargeOrder.getAccount());
    validWithdraw.setType(0);
    validWithdraw.setRechId(rechargeOrder.getId());
    validWithdraw.setRechMoney(rechargeOrder.getAmount());
    validWithdraw.setDiscountMoney(rechargeOrder.getDiscountAmount());
    validWithdraw.setDiscountDml(rechargeOrder.getDiscountDml());
    validWithdraw.setMormDml(rechargeOrder.getNormalDml());
    validWithdraw.setRemark(rechargeOrder.getRemarks());
    deleteByUserId(rechargeOrder.getMemberId(), 1);
    updateTypeByUserId(rechargeOrder.getMemberId());
    this.save(validWithdraw);
  }

  private void deleteByUserId(Long memberId, Integer status) throws Exception {
    LambdaQueryWrapper<ValidWithdraw> query = Wrappers.lambdaQuery();
    query.eq(ValidWithdraw::getMemberId, memberId)
        .eq(ValidWithdraw::getStatus, status);
    this.remove(query);
  }

  public void updateTypeByUserId(Long memberId) throws Exception {
    this.lambdaUpdate().set(ValidWithdraw::getType, 1)
        .eq(ValidWithdraw::getMemberId, memberId)
        .eq(ValidWithdraw::getType, 0).update();
  }

}
