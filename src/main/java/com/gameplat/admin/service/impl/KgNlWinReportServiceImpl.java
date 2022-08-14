package com.gameplat.admin.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.gameplat.admin.model.dto.KgNlWinReportQueryDTO;
import com.gameplat.admin.model.vo.KgNlWinReportVO;
import com.gameplat.admin.service.KgNlBetDailyDetailService;
import com.gameplat.admin.service.KgNlWinReportService;
import com.gameplat.admin.util.EasyPoiUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;

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
    String title = queryDTO.getBeginTime() + "-" + queryDTO.getEndTime() + " 彩票输赢报表";
    ExportParams exportParams = new ExportParams(title, "彩票输赢报表");
    response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename = vipWealReword.xls");
    List<KgNlWinReportVO> list = kgNlBetDailyDetailService.findWinReportData(queryDTO);
    if (ObjectUtils.isNotEmpty(queryDTO.getAccount())) {
      EasyPoiUtil.hiddenColumn("account", false, KgNlWinReportVO.class);
    }
    if (ObjectUtils.isNotEmpty(queryDTO.getProxyAccount())) {
      EasyPoiUtil.hiddenColumn("proxyAccount", false, KgNlWinReportVO.class);
      EasyPoiUtil.hiddenColumn("isDirectlyStr", false, KgNlWinReportVO.class);
    }
    try (Workbook workbook = ExcelExportUtil.exportExcel(exportParams, KgNlWinReportVO.class, list)) {
      workbook.write(response.getOutputStream());
    } catch (IOException e) {
      log.error("彩票输赢报表导出IO异常", e);
    }

    EasyPoiUtil.hiddenColumn("account", true, KgNlWinReportVO.class);
    EasyPoiUtil.hiddenColumn("proxyAccount", true, KgNlWinReportVO.class);
    EasyPoiUtil.hiddenColumn("isDirectlyStr", true, KgNlWinReportVO.class);
  }

}
