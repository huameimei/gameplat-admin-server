package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.domain.MemberGrowthLevel;
import com.gameplat.admin.model.domain.MemberGrowthStatis;
import com.gameplat.admin.model.dto.MemberGrowthLevelEditDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MemberGrowthLevelMapper extends BaseMapper<MemberGrowthLevel> {

    List<MemberGrowthLevel> findList(Integer limitLevel, String language);

    /**
     * 批量修改VIP等级
     */
    int batchUpdateLevel(List<MemberGrowthLevelEditDto> list);

    /**
     * 根据等级查询
     */
    String findLevelName(Integer level);

}
