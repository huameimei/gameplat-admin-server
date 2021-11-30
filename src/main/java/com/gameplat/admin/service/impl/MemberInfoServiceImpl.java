package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.MemberInfoMapper;
import com.gameplat.admin.model.domain.MemberInfo;
import com.gameplat.admin.model.vo.MemberInfoVO;
import com.gameplat.admin.service.MemberInfoService;
import com.gameplat.admin.service.MemberService;
import com.gameplat.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

@Slf4j
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class MemberInfoServiceImpl extends ServiceImpl<MemberInfoMapper, MemberInfo>
        implements MemberInfoService {

    @Autowired
    private MemberService memberService;

    @Override
    public void updateMemberWithdraw(MemberInfo memberInfo, BigDecimal cashMoney) {
        LambdaUpdateWrapper<MemberInfo> update = Wrappers.lambdaUpdate();
        if (memberInfo.getTotalRechTimes() == 0) {
            update
                    .set(MemberInfo::getFirstWithdrawTime, new Date())
                    .set(MemberInfo::getFirstWithdrawAmount, cashMoney);
        }
        update
                .set(MemberInfo::getLastWithdrawTime, new Date())
                .set(MemberInfo::getLastWithdrawAmount, cashMoney)
                .set(MemberInfo::getTotalWithdrawTimes, memberInfo.getTotalWithdrawTimes() + 1)
                .set(MemberInfo::getTotalWithdrawAmount, memberInfo.getTotalWithdrawAmount().add(cashMoney))
                .eq(MemberInfo::getMemberId, memberInfo.getMemberId());
        if (!this.update(update)) {
            log.error("修改会员信息异常：MemberInfo=" + memberInfo.toString());
            throw new ServiceException("UW/UPDATE_ERROR, 修改会员信息异常", null);
        }
    }

    @Override
    public void updateMemberRech(MemberInfo memberInfo, BigDecimal amount) {
        LambdaUpdateWrapper<MemberInfo> update = Wrappers.lambdaUpdate();
        if (memberInfo.getTotalRechTimes() == 0) {
            update
                    .set(MemberInfo::getFirstRechTime, new Date())
                    .set(MemberInfo::getFirstRechAmount, amount);
        }
        update
                .set(MemberInfo::getLastRechTime, new Date())
                .set(MemberInfo::getLastRechAmount, amount)
                .set(MemberInfo::getTotalRechAmount, memberInfo.getTotalRechAmount().add(amount))
                .set(MemberInfo::getTotalRechTimes, memberInfo.getTotalRechTimes() + 1)
                .eq(MemberInfo::getMemberId, memberInfo.getMemberId());
        if (!this.update(update)) {
            log.error("修改会员信息异常：MemberInfo=" + memberInfo.toString());
            throw new ServiceException("UW/UPDATE_ERROR, 修改会员信息异常", null);
        }
    }

    @Override
    public BigDecimal getUserbalance(String username) {
        MemberInfoVO memberInfo = memberService.getMemberInfo(username);
        //默认保存的是分
        return new BigDecimal(memberInfo.getBalance()).divide(BigDecimal.valueOf(100));
    }
}
