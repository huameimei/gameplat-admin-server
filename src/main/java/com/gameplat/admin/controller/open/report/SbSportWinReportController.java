package com.gameplat.admin.controller.open.report;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.enums.TimeTypeEnum;
import com.gameplat.admin.model.dto.GameBetRecordQueryDTO;
import com.gameplat.admin.model.dto.SbSportWinReportQueryDTO;
import com.gameplat.admin.model.vo.GameBetRecordVO;
import com.gameplat.admin.model.vo.HgSportWinReportVO;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.admin.service.GameBetRecordInfoService;
import com.gameplat.admin.service.SbSportWinReportService;
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
 * @author aBen
 * @date 2022/4/17 19:28
 * @desc 沙巴输赢报表
 */
@Slf4j
@Tag(name = "沙巴输赢报表")
@RestController
@RequestMapping("/api/admin/report/sb")
public class SbSportWinReportController {

  @Autowired
  private SbSportWinReportService sbSportWinReportService;

  @Autowired
  private GameBetRecordInfoService gameBetRecordInfoService;

  @GetMapping(value = "/findList")
  @PreAuthorize("hasAuthority('report:sb:view')")
  @Operation(summary = "查询沙巴输赢报表")
  public List<HgSportWinReportVO> findList(@Valid SbSportWinReportQueryDTO queryDTO) {
    return sbSportWinReportService.findList(queryDTO);
  }

  @GetMapping(value = "/exportReport")
  @PreAuthorize("hasAuthority('report:sb:export')")
  @Operation(summary = "导出沙巴输赢报表")
  public void exportReport(@Valid SbSportWinReportQueryDTO queryDTO, HttpServletResponse response) {
    sbSportWinReportService.exportReport(queryDTO, response);
  }

  @GetMapping(value = "/findDetail")
  @PreAuthorize("hasAuthority('report:sb:detail')")
  @Operation(summary = "查询沙巴报表注单详情")
  public PageDtoVO<GameBetRecordVO> findDetail(
      Page<GameBetRecordVO> page, GameBetRecordQueryDTO dto) {
    dto.setGameKind(GameKindEnum.SB_SPORT.code());
    dto.setSettle(SettleStatusEnum.YES.getValue().toString());
    dto.setTimeType(TimeTypeEnum.STAT_TIME.getValue());
    return gameBetRecordInfoService.queryPageBetRecord(page, dto);
  }
}
