package com.gameplat.admin.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.gameplat.admin.model.dto.KgNlWinReportQueryDTO;
import com.gameplat.admin.model.vo.KgNlWinReportVO;
import com.gameplat.admin.service.KgNlBetDailyDetailService;
import com.gameplat.admin.service.KgNlWinReportService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author aBen
 * @date 2022/4/17 19:34
 * @desc
 */
@Service
@Slf4j
public class KgNlWinReportServiceImpl implements KgNlWinReportService {

  @Resource
  private KgNlBetDailyDetailService kgNlBetDailyDetailService;

  @Override
  public List<KgNlWinReportVO> findList(KgNlWinReportQueryDTO queryDTO) {
    return kgNlBetDailyDetailService.findWinReportData(queryDTO);
  }

  @Override
  public void exportReport(KgNlWinReportQueryDTO queryDTO, HttpServletResponse response) {
    ExportParams exportParams = new ExportParams("彩票输赢报表", "彩票输赢报表");
    response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename = vipWealReword.xls");
    List<KgNlWinReportVO> list = kgNlBetDailyDetailService.findWinReportData(queryDTO);
    try (Workbook workbook = ExcelExportUtil.exportExcel(exportParams, KgNlWinReportVO.class, list)) {
      workbook.write(response.getOutputStream());
    } catch (IOException e) {
      log.error("彩票输赢报表导出IO异常", e);
    }
  }

}
