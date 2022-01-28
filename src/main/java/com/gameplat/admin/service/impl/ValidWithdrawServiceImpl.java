package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.ValidWithdrawMapper;
import com.gameplat.admin.model.domain.RechargeOrder;
import com.gameplat.admin.model.domain.ValidWithdraw;
import com.gameplat.admin.service.ValidWithdrawService;
import java.util.Date;
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
        validWithdraw.setRechId(rechargeOrder.getId().toString());
        validWithdraw.setRechMoney(rechargeOrder.getPayAmount());
        validWithdraw.setDiscountMoney(rechargeOrder.getDiscountAmount());
        validWithdraw.setDiscountDml(rechargeOrder.getDiscountDml());
        validWithdraw.setMormDml(rechargeOrder.getNormalDml());
        validWithdraw.setRemark(rechargeOrder.getRemarks());
        deleteByUserId(rechargeOrder.getMemberId(), 1);
        updateTypeByUserId(rechargeOrder.getMemberId());
        this.save(validWithdraw);
    }

    @Override
    public void remove(Long memberId, Date cashDate) throws Exception {
        // 申请提现至出款期间如果有新的充值，出款时直接删除申请提现之前的打码量，而不是更新status为1
        LambdaQueryWrapper<ValidWithdraw> query = Wrappers.lambdaQuery();
        query.eq(ValidWithdraw::getMemberId, memberId)
                .gt(ValidWithdraw::getCreateTime, cashDate);
        if (this.count(query) > 0) {
            this.remove(query);
        } else {
            this.lambdaUpdate().set(ValidWithdraw::getStatus, 1)
                    .eq(ValidWithdraw::getMemberId, memberId)
                    .le(ValidWithdraw::getCreateTime, cashDate).update();
        }
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

    @Override
    public int saveValidWithdraw(ValidWithdraw validWithdraw) {
        //根据会员id查找
        ValidWithdraw query = new ValidWithdraw();
        query.setMemberId(validWithdraw.getMemberId());
        ValidWithdraw validWithdraw1 = validWithdrawMapper.findValidWithdraw(query);

        int save = validWithdrawMapper.save(validWithdraw);
        if (save > 0) {
            if (validWithdraw1 != null) {
                validWithdraw1.setUpdateTime(new Date());
                validWithdrawMapper.updateByUserId(validWithdraw1);
            }

        }
        return save;
    }

}
