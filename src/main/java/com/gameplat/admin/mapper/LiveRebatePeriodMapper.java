package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.domain.LiveRebatePeriod;
import com.gameplat.admin.model.vo.LiveRebatePeriodVO;
import java.util.List;

public interface LiveRebatePeriodMapper extends BaseMapper<LiveRebatePeriod> {

  List<LiveRebatePeriodVO> queryLiveRebateCount();
}
