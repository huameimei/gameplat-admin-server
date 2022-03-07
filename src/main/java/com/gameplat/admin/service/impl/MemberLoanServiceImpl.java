package com.gameplat.admin.service.impl;

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
import com.gameplat.admin.service.MemberGrowthConfigService;
import com.gameplat.admin.service.MemberGrowthLevelService;
import com.gameplat.admin.service.MemberLoanService;
import com.gameplat.admin.service.MemberService;
import com.gameplat.model.entity.member.Member;
import com.gameplat.model.entity.member.MemberGrowthLevel;
import com.gameplat.model.entity.member.MemberLoan;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.ap.internal.model.assignment.UpdateWrapper;
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
    private MemberGrowthLevelService memberGrowthLevelService;

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
        IPage<MemberLoanVO> memberLoanVO = this.lambdaQuery()
                .eq(ObjectUtil.isNotNull(dto.getAccount()), MemberLoan::getAccount, dto.getAccount())
                .eq(ObjectUtil.isNotNull(dto.getVipLevel()), MemberLoan::getVipLevel, dto.getVipLevel())
                .ge(ObjectUtil.isNotNull(dto.getMinOverdraftMoney()), MemberLoan::getOverdraftMoney, dto.getMinOverdraftMoney())
                .le(ObjectUtil.isNotNull(dto.getMaxOverdraftMoney()), MemberLoan::getOverdraftMoney, dto.getMaxOverdraftMoney())
                .ge(ObjectUtil.isNotNull(dto.getBeginLoanTime()), MemberLoan::getLoanTime, dto.getBeginLoanTime())
                .le(ObjectUtil.isNotNull(dto.getEndLoanTime()), MemberLoan::getLoanTime, dto.getEndLoanTime())
                .eq(ObjectUtil.isNotNull(dto.getLoanStatus()), MemberLoan::getLoanTime, dto.getLoanStatus())
                .page(page)
                .convert(memberLoanConvert::toVo);

        return memberLoanVO;
    }

    @Override
    public void editOrUpdate(MemberLoan memberLoan) {
        MemberLoan loan = this.lambdaQuery()
                .eq(MemberLoan::getMemberId, memberLoan.getMemberId())
                .one();
        if(ObjectUtil.isNotNull(loan)){
            LambdaUpdateWrapper<MemberLoan> wrapper = new LambdaUpdateWrapper();
            wrapper.set(MemberLoan::getLoanMoney, memberLoan.getLoanMoney())
                    .eq(MemberLoan::getMemberId, memberLoan.getMemberId());
            this.update(wrapper);
        }else{
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
}
