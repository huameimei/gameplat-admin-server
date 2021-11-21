package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.LiveRebateDetail;
import com.gameplat.admin.model.domain.LiveRebatePeriod;
import java.rmi.ServerException;
import java.util.List;

public interface LiveRebateDetailService extends IService<LiveRebateDetail> {

  List<LiveRebateDetail> liveRebateDetailByStatus(Long periodId, int status);

  void deleteByPeriodId(Long periodId);

  void createLiveRebateDetail(LiveRebatePeriod liveRebatePeriod);
}
