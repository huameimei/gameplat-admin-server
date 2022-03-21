package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.LotteryRebateReportMapper;
import com.gameplat.admin.model.dto.LotteryRebateReportDTO;
import com.gameplat.admin.model.vo.LotteryRebateReportVO;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.admin.service.LotteryRebateReportService;
import com.gameplat.model.entity.report.LotteryRebateReport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class LotteryRebateReportServiceImpl
    extends ServiceImpl<LotteryRebateReportMapper, LotteryRebateReport>
    implements LotteryRebateReportService {

  @Autowired private LotteryRebateReportMapper lotteryRebateReportMapper;

  @Override
  public PageDtoVO<LotteryRebateReportVO> page(
      PageDTO<LotteryRebateReport> page, LotteryRebateReportDTO dto) {
    PageDtoVO<LotteryRebateReportVO> pageDtoVO = new PageDtoVO<>();
    Page<LotteryRebateReportVO> result = lotteryRebateReportMapper.pageList(page, dto);
    LotteryRebateReportVO total = lotteryRebateReportMapper.sumForList(dto);
    Map<String, Object> otherData = new HashMap<String, Object>();
    otherData.put("totalData", total);
    pageDtoVO.setPage(result);
    pageDtoVO.setOtherData(otherData);
    return pageDtoVO;
  }
}
