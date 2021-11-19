package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.LiveRebateDetail;
import com.gameplat.admin.model.domain.LiveRebatePeriod;
import com.gameplat.admin.model.domain.LiveRebateReport;
import com.gameplat.admin.model.dto.LiveRebateReportQueryDTO;
import com.gameplat.admin.model.vo.PageDtoVO;
import java.math.BigDecimal;
import java.util.List;

public interface LiveRebateReportService extends IService<LiveRebateReport> {

  void accept(Long periodId, Long memberId, BigDecimal realRebateMoney, String remark);
  
  void deleteByPeriodId(Long periodId);

  void createForLiveRebatePeriod(LiveRebatePeriod liveRebatePeriod);

  void rollBack(Long memberId, Long periodId, String periodName, BigDecimal realRebateMoney, String remark);

  PageDtoVO<LiveRebateDetail> queryPage(Page<LiveRebateDetail> page, LiveRebateReportQueryDTO dto);

  void reject(String account, String periodName, String remark);

  void modify(Long id, BigDecimal realRebateMoney, String remark);

  List<LiveRebateReport> queryDetail(LiveRebateReportQueryDTO dto);
}
