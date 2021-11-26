package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.SysBannerInfoMapper;
import com.gameplat.admin.model.domain.SysBannerInfo;
import com.gameplat.admin.service.SysBannerInfoService;

import java.util.List;

/**
 * @author admin
 */
public class SysBannerInfoServiceImpl
        extends ServiceImpl<SysBannerInfoMapper, SysBannerInfo> implements SysBannerInfoService {


    @Override
    public List<SysBannerInfo> getByBanner(SysBannerInfo banner) {
        return this.lambdaQuery().eq(banner.getStatus() == null && banner.getStatus() != 0
                , SysBannerInfo::getStatus, banner.getStatus()).list();
    }

    @Override
    public boolean save(SysBannerInfo entity) {
        return this.save(entity);
    }
}
