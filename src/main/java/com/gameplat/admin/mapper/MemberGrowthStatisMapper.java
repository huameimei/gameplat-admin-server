package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.domain.MemberGrowthStatis;
import com.gameplat.admin.model.domain.MemberWealDetail;

import java.util.List;

public interface MemberGrowthStatisMapper extends BaseMapper<MemberGrowthStatis> {

    /**
     * 获取达到有效投注金额的会员账号
     */
    List<MemberWealDetail> getMemberSalaryInfo(Integer type);


    int insertOrUpdate(MemberGrowthStatis memberGrowthStatis);
}
