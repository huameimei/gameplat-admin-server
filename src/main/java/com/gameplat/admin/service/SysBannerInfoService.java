package com.gameplat.admin.service;

import com.gameplat.admin.model.domain.SysBannerInfo;

import java.util.List;

/**
 * banner业务类
 */
public interface SysBannerInfoService {
    /**
     * 查询banner列表
     *
     * @param banner
     * @return
     */
    List<SysBannerInfo> getByBanner(SysBannerInfo banner);

    /**
     * banner信息保存
     *
     * @param sysBannerInfo
     */
    boolean save(SysBannerInfo sysBannerInfo);
}
