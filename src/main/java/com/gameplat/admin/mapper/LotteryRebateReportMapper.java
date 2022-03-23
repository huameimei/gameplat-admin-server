package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.LotteryRebateReportDTO;
import com.gameplat.admin.model.vo.LotteryRebateReportVO;
import com.gameplat.model.entity.report.LotteryRebateReport;
import org.apache.ibatis.annotations.Param;

public interface LotteryRebateReportMapper extends BaseMapper<LotteryRebateReport> {
  Page<LotteryRebateReportVO> pageList(
      PageDTO<LotteryRebateReport> page, @Param("dto") LotteryRebateReportDTO rebateReportDTO);

  LotteryRebateReportVO sumForList(LotteryRebateReportDTO dto);
}
