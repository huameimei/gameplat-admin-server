package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.LotteryRebateReportDTO;
import com.gameplat.admin.model.vo.LotteryRebateReportVO;
import com.gameplat.model.entity.report.LotteryRebateReport;
import org.apache.ibatis.annotations.Param;

/** @Description : KG新彩票代理返点 @Author : cc @Date : 2022/3/21 */
public interface LotteryRebateReportMapper extends BaseMapper<LotteryRebateReport> {
  /**
   * 分页列表
   *
   * @param page
   * @param rebateReportDTO
   * @return
   */
  Page<LotteryRebateReportVO> pageList(
      PageDTO<LotteryRebateReport> page, @Param("dto") LotteryRebateReportDTO rebateReportDTO);

  /**
   * 汇总代理返点列表
   *
   * @param dto
   * @return
   */
  LotteryRebateReportVO sumForList(LotteryRebateReportDTO dto);
}
