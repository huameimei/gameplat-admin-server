package com.gameplat.admin.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.MemberLoanConvert;
import com.gameplat.admin.mapper.MemberLoanMapper;
import com.gameplat.admin.model.dto.MemberLoanQueryDTO;
import com.gameplat.admin.model.dto.MemberQueryDTO;
import com.gameplat.admin.model.vo.MemberLoanVO;
import com.gameplat.admin.model.vo.MemberVO;
import com.gameplat.admin.service.*;
import com.gameplat.base.common.context.GlobalContextHolder;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.common.enums.TranTypes;
import com.gameplat.model.entity.member.*;
import com.gameplat.model.entity.message.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lily
 * @description
 * @date 2022/3/6
 */
@Slf4j
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class MemberLoanServiceImpl extends ServiceImpl<MemberLoanMapper, MemberLoan> implements MemberLoanService {

    @Autowired
    private MemberLoanConvert memberLoanConvert;
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberInfoService memberInfoService;
    @Autowired
    private MemberGrowthLevelService memberGrowthLevelService;
    @Autowired
    private MemberBillService memberBillService;
    @Autowired
    private MessageInfoService messageInfoService;

    /**
     * 分页查
     */
    @Override
    public IPage<MemberLoanVO> page(PageDTO<MemberLoan> page, MemberLoanQueryDTO dto) {

        //获取符合资格的会员
        List<MemberGrowthLevel> memberGrowthLevelList = memberGrowthLevelService.lambdaQuery()
                .gt(MemberGrowthLevel::getLoanMoney, 0.00)
                .list();
        List<MemberVO> memberList = new ArrayList<>();
        for (MemberGrowthLevel memberGrowthLevel : memberGrowthLevelList) {
            List<MemberVO> memberVoList = memberService.queryList(new MemberQueryDTO() {{
                setVipLevel(memberGrowthLevel.getLevel());
            }});
            for (MemberVO memberVO : memberVoList) {
                memberList.add(memberVO);
            }
            for (MemberVO memberVO : memberList) {
                //已经有数据就不添加
                MemberLoan memberLoan = this.lambdaQuery().eq(MemberLoan::getMemberId, memberVO.getId()).one();
                if (ObjectUtil.isNull(memberLoan)) {
                    MemberLoan saveMemberLoan = new MemberLoan()
                            .setMemberId(memberVO.getId())
                            .setAccount(memberVO.getAccount())
                            .setUserLevel(memberVO.getUserLevel())
                            .setSuperPath(memberVO.getSuperPath())
                            .setParentId(memberVO.getParentId())
                            .setParentName(memberVO.getParentName())
                            .setVipLevel(memberVO.getVipLevel())
                            .setMemberBalance(memberVO.getBalance())
                            .setLoanMoney(memberGrowthLevel.getLoanMoney())
                            .setLoanStatus(1)
                            .setOverdraftMoney(new BigDecimal(0.00));
                    this.save(saveMemberLoan);
                }
            }
        }
        IPage<MemberLoanVO> pageList = this.lambdaQuery()
                .eq(ObjectUtil.isNotNull(dto.getAccount()), MemberLoan::getAccount, dto.getAccount())
                .eq(ObjectUtil.isNotNull(dto.getVipLevel()), MemberLoan::getVipLevel, dto.getVipLevel())
                .ge(ObjectUtil.isNotNull(dto.getMinOverdraftMoney()), MemberLoan::getOverdraftMoney, dto.getMinOverdraftMoney())
                .le(ObjectUtil.isNotNull(dto.getMaxOverdraftMoney()), MemberLoan::getOverdraftMoney, dto.getMaxOverdraftMoney())
                .ge(ObjectUtil.isNotNull(dto.getBeginLoanTime()), MemberLoan::getLoanTime, dto.getBeginLoanTime())
                .le(ObjectUtil.isNotNull(dto.getEndLoanTime()), MemberLoan::getLoanTime, dto.getEndLoanTime())
                .eq(ObjectUtil.isNotNull(dto.getLoanStatus()), MemberLoan::getLoanStatus, dto.getLoanStatus())
                .page(page)
                .convert(memberLoanConvert::toVo);
        BigDecimal totalOverdraftMoney = new BigDecimal(0.00);
        List<MemberLoanVO> records = pageList.getRecords();
        for (MemberLoanVO record : records) {
            totalOverdraftMoney = totalOverdraftMoney.add(record.getOverdraftMoney());
        }
        records.get(0).setTotalOverdraftMoney(totalOverdraftMoney);
        return pageList;
    }

    @Override
    public void editOrUpdate(MemberLoan memberLoan) {
        MemberLoan loan = this.lambdaQuery()
                .eq(MemberLoan::getMemberId, memberLoan.getMemberId())
                .one();
        if (ObjectUtil.isNotNull(loan)) {
            LambdaUpdateWrapper<MemberLoan> wrapper = new LambdaUpdateWrapper<>();
            wrapper.set(MemberLoan::getLoanMoney, memberLoan.getLoanMoney())
                    .eq(MemberLoan::getMemberId, memberLoan.getMemberId());
            this.update(wrapper);
        } else {
            MemberVO member = memberService.queryList(new MemberQueryDTO() {{
                setId(memberLoan.getMemberId());
            }}).get(0);
            memberLoan.setUserLevel(member.getUserLevel())
                    .setSuperPath(member.getSuperPath())
                    .setParentId(member.getParentId())
                    .setParentName(member.getParentName())
                    .setVipLevel(member.getVipLevel())
                    .setMemberBalance(member.getBalance())
                    .setLoanStatus(1)
                    .setOverdraftMoney(new BigDecimal(0.00));
            this.save(memberLoan);
        }

    }

    /**
     * 回收
     */
    @Override
    public void recycle(String idList) {
        String username = GlobalContextHolder.getContext().getUsername();
        String[] split = idList.split(",");
        for (String loanId : split) {
            Long id = Long.parseLong(loanId);
            MemberLoan memberLoan = this.lambdaQuery()
                    .eq(MemberLoan::getId, id)
                    .one();
            if (ObjectUtil.isNull(memberLoan)) {
                throw new ServiceException("此账号暂无法使用借呗功能");
            }
            if (memberLoan.getLoanStatus() == 1) {
                throw new ServiceException("此账号暂无欠款");
            } else if (memberLoan.getLoanStatus() == 2) {
                throw new ServiceException("此账号欠款已回收");
            }
            //欠款金额
            BigDecimal overdraftMoney = memberLoan.getOverdraftMoney();
            MemberInfo memberInfo = memberInfoService.lambdaQuery()
                    .eq(MemberInfo::getMemberId, memberLoan.getMemberId())
                    .one();
            BigDecimal balance = memberInfo.getBalance();
            BigDecimal newBalance = balance.subtract(overdraftMoney);
            Member member = memberService.getById(memberLoan.getMemberId());
            if (balance.compareTo(overdraftMoney) < 0) {
                throw new ServiceException(member.getAccount() + " 余额不足，扣款失败!");
            }
            this.updateById(new MemberLoan() {{
                setId(id);
                setLoanStatus(3);
                setOverdraftMoney(new BigDecimal(0.00));
                setMemberBalance(newBalance);
            }});
            memberInfoService.update(new LambdaUpdateWrapper<MemberInfo>()
                                                                    .set(MemberInfo::getBalance, newBalance)
                                                                    .eq(MemberInfo::getMemberId, memberLoan.getMemberId())
            );

            MemberBill memberBill = new MemberBill();
            memberBill.setMemberId(memberLoan.getMemberId());
            memberBill.setAccount(member.getAccount());
            memberBill.setMemberPath(member.getSuperPath());
            memberBill.setTranType(TranTypes.LOAN_RECYCLE.getValue());
            memberBill.setAmount(overdraftMoney);
            memberBill.setBalance(balance);
            memberBill.setContent("管理员:" + username + " 于 " + DateUtil.now() + "回收借呗欠款 " + overdraftMoney);
            memberBill.setRemark("回收欠款");
            memberBill.setOperator(username);
            memberBillService.save(memberBill);

            Message message = new Message();
            message.setTitle("借呗欠款回收");
            message.setContent("借呗欠款：" + overdraftMoney + " 已回收");
            message.setCategory(4);
            message.setPushRange(2);
            message.setLinkAccount(member.getAccount());
            message.setType(1);
            message.setStatus(1);
            message.setRemarks("借呗欠款回收");
            messageInfoService.save(message);

        }

    }
}
