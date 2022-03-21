package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.LotteryRebateReportDTO;
import com.gameplat.admin.model.vo.LotteryRebateReportVO;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.model.entity.report.LotteryRebateReport;

public interface LotteryRebateReportService extends IService<LotteryRebateReport> {
  PageDtoVO<LotteryRebateReportVO> page(
      PageDTO<LotteryRebateReport> page, LotteryRebateReportDTO dto);
}
