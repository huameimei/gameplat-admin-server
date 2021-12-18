package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.domain.MemberGrowthRecord;

import java.util.List;

/**
 * @author Lily
 */
public interface MemberGrowthRecordMapper extends BaseMapper<MemberGrowthRecord> {

    List<MemberGrowthRecord> findRecordGroupBy(MemberGrowthRecord entity);
}
