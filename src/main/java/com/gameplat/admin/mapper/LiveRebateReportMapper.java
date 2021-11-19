package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.domain.LiveRebateReport;
import java.util.List;

public interface LiveRebateReportMapper extends BaseMapper<LiveRebateReport> {

  List<LiveRebateReport> queryLiveRebateReportByStatus(Long periodId,int status);

}
