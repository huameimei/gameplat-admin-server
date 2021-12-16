package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.domain.ActivityPrize;
import com.gameplat.admin.model.vo.MemberActivityPrizeVO;

import java.util.List;

public interface ActivityPrizeMapper extends BaseMapper<ActivityPrize> {
    List<MemberActivityPrizeVO> findActivityPrizeList(MemberActivityPrizeVO memberActivityPrizeBean);
}
