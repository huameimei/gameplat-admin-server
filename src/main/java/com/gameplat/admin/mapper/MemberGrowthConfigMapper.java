package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.domain.MemberGrowthConfig;

public interface MemberGrowthConfigMapper extends BaseMapper<MemberGrowthConfig> {

    MemberGrowthConfig findOneConfig(String language);
}
