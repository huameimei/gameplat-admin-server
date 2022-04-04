package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.LotteryRebateReportDTO;
import com.gameplat.admin.model.vo.LotteryRebateReportVO;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.model.entity.report.LotteryRebateReport;

/** @Description : KG新彩票代理返点 @Author : cc @Date : 2022/3/21 */
public interface LotteryRebateReportService extends IService<LotteryRebateReport> {
  /**
   * 分页列表
   *
   * @param page
   * @param dto
   * @return
   */
  PageDtoVO<LotteryRebateReportVO> page(
      PageDTO<LotteryRebateReport> page, LotteryRebateReportDTO dto);
}
