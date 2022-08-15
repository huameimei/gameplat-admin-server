package com.gameplat.admin.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.KgNlBetDailyDetailMapper;
import com.gameplat.admin.model.dto.KgNlWinReportQueryDTO;
import com.gameplat.admin.model.vo.KgNlBetDailyDetailVO;
import com.gameplat.admin.model.vo.KgNlWinReportVO;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.admin.service.KgNlBetDailyDetailService;
import com.gameplat.model.entity.game.KgNlBetDailyDetail;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class KgNlBetDailyDetailServiceImpl extends ServiceImpl<KgNlBetDailyDetailMapper, KgNlBetDailyDetail>
  implements KgNlBetDailyDetailService {

  @Autowired
  private KgNlBetDailyDetailMapper kgNlBetDailyDetailMapper;

  public List<KgNlWinReportVO> findWinReportData(KgNlWinReportQueryDTO dto) {
    return kgNlBetDailyDetailMapper.findWinReportData(dto);
  }

  public PageDtoVO<KgNlBetDailyDetailVO> findDetailPage(Page<KgNlBetDailyDetailVO> page, KgNlWinReportQueryDTO dto) {
    PageDtoVO<KgNlBetDailyDetailVO> pageDtoVO = new PageDtoVO<>();
    Page<KgNlBetDailyDetailVO> list = kgNlBetDailyDetailMapper.findDetailPage(page, dto);
    if (ObjectUtil.isEmpty(list)) {
      return pageDtoVO;
    }

    // 查询总计
    KgNlBetDailyDetail totalData = kgNlBetDailyDetailMapper.findTotalData(dto);

    Map<String, Object> otherData = new HashMap<>();
    otherData.put("totalData", totalData);
    pageDtoVO.setPage(list);
    pageDtoVO.setOtherData(otherData);
    return pageDtoVO;
  }

  @Override
  public void exportDetail(HttpServletResponse response, KgNlWinReportQueryDTO dto) {
    String title = dto.getBeginTime() + "-" + dto.getEndTime() + " 彩票输赢详情";
    ExportParams exportParams = new ExportParams(title, "彩票输赢详情");
    response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename = vipWealReword.xls");
    List<KgNlBetDailyDetailVO> list = kgNlBetDailyDetailMapper.findDetailList(dto);
    try (Workbook workbook = ExcelExportUtil.exportExcel(exportParams, KgNlBetDailyDetailVO.class, list)) {
      workbook.write(response.getOutputStream());
    } catch (IOException e) {
      log.error("彩票输赢详情导出IO异常", e);
    }
  }
}
