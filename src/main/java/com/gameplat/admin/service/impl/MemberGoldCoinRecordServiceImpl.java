package com.gameplat.admin.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.MemberGoldCoinRecordConvert;
import com.gameplat.admin.mapper.MemberGoldCoinRecordMapper;
import com.gameplat.model.entity.member.MemberGoldCoinRecord;
import com.gameplat.admin.model.dto.MemberGoldCoinRecordQueryDTO;
import com.gameplat.admin.model.vo.MemberGoldCoinRecordVO;
import com.gameplat.admin.model.vo.MemberInfoVO;
import com.gameplat.admin.service.MemberGoldCoinRecordService;
import com.gameplat.admin.service.MemberInfoService;
import com.gameplat.admin.service.MemberService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.model.entity.member.MemberInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lily
 * @description
 * @date 2022/3/1
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class MemberGoldCoinRecordServiceImpl extends ServiceImpl<MemberGoldCoinRecordMapper, MemberGoldCoinRecord> implements MemberGoldCoinRecordService {

    @Autowired
    private MemberGoldCoinRecordConvert memberGoldCoinRecordConvert;
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberInfoService memberInfoService;

    /** 分页查 */
    @Override
    public IPage<MemberGoldCoinRecordVO> page(PageDTO<MemberGoldCoinRecord> page, MemberGoldCoinRecordQueryDTO dto) {
        return this.lambdaQuery()
                .eq(ObjectUtil.isNotEmpty(dto.getAccount()), MemberGoldCoinRecord::getAccount, dto.getAccount())
                .eq(ObjectUtil.isNotEmpty(dto.getSourceType()), MemberGoldCoinRecord::getSourceType, dto.getSourceType())
                .ge(ObjectUtil.isNotEmpty(dto.getStartTime()), MemberGoldCoinRecord::getCreateTime, dto.getStartTime())
                .le(ObjectUtil.isNotEmpty(dto.getEndTime()), MemberGoldCoinRecord::getCreateTime, dto.getEndTime())
                .page(page)
                .convert(memberGoldCoinRecordConvert::toVo);
    }

    @Override
    public void addGoldCoin(Long memberId, Integer amount){
        MemberInfo memberInfo = memberInfoService.lambdaQuery().eq(MemberInfo::getMemberId, memberId).one();
        if(null == memberInfo){
            throw new ServiceException("此账号不存在！");
        }
        add(memberId, amount);
        LambdaUpdateWrapper<MemberInfo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(MemberInfo::getGoldCoin, amount + memberInfo.getGoldCoin())
                .eq(MemberInfo::getMemberId, memberId);
        memberInfoService.update(wrapper);
    }

    /** 增 */
    @Override
    public void add(Long memberId, Integer amount) {
        log.info("系统增加金币："+ memberId+",金币数："+amount);
        if (ObjectUtil.isNotEmpty(amount)) {
            throw new ServiceException("增加金币数量不能为空");
        }
        if (ObjectUtil.isNotEmpty(memberId)) {
            throw new ServiceException("玩家不能为空");
        }
        MemberInfoVO member = memberService.getInfo(memberId);
        if (ObjectUtil.isNotEmpty(member)){
            throw new ServiceException("未找到相关玩家");
        }
        String remark = "后台添加金币，用户：" + member.getAccount() + ",金币：" + amount;

        this.save( MemberGoldCoinRecord.builder()
                                        .memberId(memberId.longValue())
                                        .account(member.getAccount())
                                        .sourceType(3)
                                        .amount(amount)
                                        .beforeBalance(member.getGoldCoin())
                                        .afterBalance(member.getGoldCoin() + amount)
                                        .remark(remark)
                                        .build()
        );

        memberInfoService.updateById(new MemberInfo(){{
            setMemberId(memberId);
            setGoldCoin(member.getGoldCoin() + amount);
        }});
    }
}
