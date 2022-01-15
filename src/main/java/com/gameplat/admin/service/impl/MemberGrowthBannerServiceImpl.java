package com.gameplat.admin.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.MemberGrowthBannerConvert;
import com.gameplat.admin.mapper.MemberGrowthBannerMapper;
import com.gameplat.admin.model.domain.MemberGrowthBanner;
import com.gameplat.admin.model.dto.MemberGrowthBannerAddDTO;
import com.gameplat.admin.model.dto.MemberGrowthBannerEditDTO;
import com.gameplat.admin.model.vo.MemberGrowthBannerVO;
import com.gameplat.admin.service.MemberGrowthBannerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author lily
 * @description VIP banner图配置
 * @date 2022/1/15
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class MemberGrowthBannerServiceImpl extends ServiceImpl<MemberGrowthBannerMapper, MemberGrowthBanner> implements MemberGrowthBannerService {

    private final MemberGrowthBannerConvert memberGrowthBannerConvert;

    /** 增 */
    @Override
    public void addBanner(MemberGrowthBannerAddDTO dto) {
        this.save(memberGrowthBannerConvert.toEntity(dto));
    }

    /** 删 */
    @Override
    public void remove(Long id) {
        this.removeById(id);
    }

    /** 改 */
    @Override
    public void updateBanner(MemberGrowthBannerEditDTO dto) {
        this.updateById(memberGrowthBannerConvert.toEntity(dto));
    }

    /** 查 */
    @Override
    public IPage<MemberGrowthBannerVO> getList(PageDTO<MemberGrowthBanner> page, MemberGrowthBannerAddDTO dto) {
        return this.lambdaQuery()
                .eq(ObjectUtil.isNotEmpty(dto.getCilentType()), MemberGrowthBanner::getCilentType, dto.getCilentType())
                .eq(ObjectUtil.isNotEmpty(dto.getAreaType()), MemberGrowthBanner::getAreaType, dto.getAreaType())
                .page(page)
                .convert(memberGrowthBannerConvert::toVo);
    }
}
