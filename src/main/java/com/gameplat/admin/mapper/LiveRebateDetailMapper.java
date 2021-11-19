package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.domain.LiveRebateDetail;
import com.gameplat.admin.model.dto.LiveRebateReportQueryDTO;

public interface LiveRebateDetailMapper extends BaseMapper<LiveRebateDetail> {

  LiveRebateDetail queryAllLiveRebateReport(LiveRebateReportQueryDTO dto);
}