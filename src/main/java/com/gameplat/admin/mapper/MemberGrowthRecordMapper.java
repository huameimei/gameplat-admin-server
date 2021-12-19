package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.domain.MemberGrowthRecord;
import com.gameplat.admin.model.domain.MemberWealDetail;

import java.util.List;

/**
 * @author Lily
 */
public interface MemberGrowthRecordMapper extends BaseMapper<MemberGrowthRecord> {

    List<MemberGrowthRecord> findRecordGroupBy(MemberGrowthRecord entity);

    /**
     * 获取达到有效投注金额的会员账号
     */
    List<MemberWealDetail> getMemberSalaryInfo(Integer type);
}
