package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.MemberGrowthStatisConvert;
import com.gameplat.admin.mapper.MemberGrowthStatisMapper;
import com.gameplat.admin.model.domain.MemberGrowthStatis;
import com.gameplat.admin.model.dto.MemberGrowthStatisDTO;
import com.gameplat.admin.model.vo.MemberGrowthStatisVO;
import com.gameplat.admin.service.MemberGrowthStatisService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lily
 * @description vip等级汇总业务处理层
 * @date 2021/11/24
 */

@Service
@RequiredArgsConstructor
public class MemberGrowthStatisServiceImpl extends ServiceImpl<MemberGrowthStatisMapper, MemberGrowthStatis> implements MemberGrowthStatisService {

    @Autowired
    private MemberGrowthStatisConvert statisConvert;

    @Override
    public IPage<MemberGrowthStatisVO> findStatisList(PageDTO<MemberGrowthStatis> page, MemberGrowthStatisDTO dto) {
    return
            this.lambdaQuery()
                    .like(ObjectUtils.isNotEmpty(dto.getUserName()), MemberGrowthStatis::getUserName, dto.getUserName())
                    .ge(ObjectUtils.isNotEmpty(dto.getStartTime()), MemberGrowthStatis::getCreateTime, dto.getStartTime())
                    .le(ObjectUtils.isNotEmpty(dto.getEndTime()), MemberGrowthStatis::getCreateTime, dto.getEndTime())
                    .orderByDesc(MemberGrowthStatis::getCreateTime)
                    .page(page)
                    .convert(statisConvert::toVo);
    }
}
