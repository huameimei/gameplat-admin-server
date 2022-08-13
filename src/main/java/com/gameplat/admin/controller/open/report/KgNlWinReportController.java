package com.gameplat.admin.controller.open.report;

import com.alibaba.excel.util.DateUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.enums.TimeTypeEnum;
import com.gameplat.admin.model.dto.GameBetRecordQueryDTO;
import com.gameplat.admin.model.dto.KgNlWinReportQueryDTO;
import com.gameplat.admin.model.vo.GameBetRecordVO;
import com.gameplat.admin.model.vo.KgNlWinReportVO;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.admin.service.GameBetRecordInfoService;
import com.gameplat.admin.service.KgNlWinReportService;
import com.gameplat.base.common.util.DateUtil;
import com.gameplat.common.enums.GameKindEnum;
import com.gameplat.common.enums.SettleStatusEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * KG新彩票输赢报表
 *
 * @author aBen
 * @date 2022/4/17 19:28
 */
@Slf4j
@Tag(name = "KG新彩票输赢报表")
@RestController
@RequestMapping("/api/admin/report/kgNl")
public class KgNlWinReportController {

  @Autowired
  private KgNlWinReportService kgNlWinReportService;

  @Autowired
  private GameBetRecordInfoService gameBetRecordInfoService;

  @GetMapping(value = "/findList")
  @PreAuthorize("hasAuthority('report:kgnl:view')")
  @Operation(summary = "查询KG新彩票输赢报表")
  public List<KgNlWinReportVO> findList(@Valid KgNlWinReportQueryDTO queryDTO) {
    return kgNlWinReportService.findList(queryDTO);
  }

  @GetMapping(value = "/exportReport")
  @PreAuthorize("hasAuthority('report:kgnl:export')")
  @Operation(summary = "导出KG新彩票输赢报表")
  public void exportReport(@Valid KgNlWinReportQueryDTO queryDTO, HttpServletResponse response) {
    kgNlWinReportService.exportReport(queryDTO, response);
  }

  @GetMapping(value = "/findDetail")
  @PreAuthorize("hasAuthority('report:kgnl:detail')")
  @Operation(summary = "查询KG新彩票报表注单详情")
  public PageDtoVO<GameBetRecordVO> findDetail(
    Page<GameBetRecordVO> page, GameBetRecordQueryDTO dto) {
    dto.setGameKind(GameKindEnum.KGNL_LOTTERY.code());
    dto.setSettle(SettleStatusEnum.YES.getValue().toString());
    dto.setTimeType(TimeTypeEnum.STAT_TIME.getValue());
    dto.setBeginTime(String.valueOf(
      DateUtil.strToDate(dto.getBeginTime() + " 00:00:00", "yyyy-MM-dd HH:mm:ss").getTime()));
    dto.setEndTime(String.valueOf(
      DateUtil.strToDate(dto.getEndTime() + " 23:59:59", "yyyy-MM-dd HH:mm:ss").getTime()));
    return gameBetRecordInfoService.queryPageBetRecord(page, dto);
  }
}
