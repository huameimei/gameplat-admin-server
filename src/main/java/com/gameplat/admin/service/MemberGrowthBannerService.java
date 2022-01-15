package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.MemberGrowthBanner;
import com.gameplat.admin.model.dto.MemberGrowthBannerAddDTO;
import com.gameplat.admin.model.dto.MemberGrowthBannerEditDTO;
import com.gameplat.admin.model.vo.MemberGrowthBannerVO;

public interface MemberGrowthBannerService extends IService<MemberGrowthBanner> {

    /** 增 */
    void addBanner(MemberGrowthBannerAddDTO dto);

    /** 删 */
    void remove(Long id);

    /** 改 */
    void updateBanner(MemberGrowthBannerEditDTO dto);

    /** 查 */
    IPage<MemberGrowthBannerVO> getList(PageDTO<MemberGrowthBanner> page, MemberGrowthBannerAddDTO dto);
}
