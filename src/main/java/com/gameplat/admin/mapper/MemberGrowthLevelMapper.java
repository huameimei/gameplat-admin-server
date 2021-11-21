package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.domain.MemberGrowthLevel;

import java.util.List;

public interface MemberGrowthLevelMapper extends BaseMapper<MemberGrowthLevel> {

    List<MemberGrowthLevel> findList(Integer limitLevel, String language);
}
