package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.MemberGrowthConfig;
import com.gameplat.admin.model.vo.MemberGrowthConfigVO;

public interface MemberGrowthConfigService extends IService<MemberGrowthConfig> {

    /**
     * 只查询一条
     */
    MemberGrowthConfigVO findOneConfig(String language);
}
