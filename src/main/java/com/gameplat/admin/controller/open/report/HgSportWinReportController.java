package com.gameplat.admin.controller.open.report;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.enums.TimeTypeEnum;
import com.gameplat.admin.model.dto.GameBetRecordQueryDTO;
import com.gameplat.admin.model.dto.HgSportWinReportQueryDTO;
import com.gameplat.admin.model.vo.GameBetRecordVO;
import com.gameplat.admin.model.vo.HgSportWinReportVO;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.admin.service.GameBetRecordInfoService;
import com.gameplat.admin.service.HgSportWinReportService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.common.enums.GameKindEnum;
import com.gameplat.common.enums.SettleStatusEnum;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
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
 * 皇冠输赢报表
 *
 * @author aBen
 * @date 2022/4/17 19:28
 */
@Slf4j
@Tag(name = "皇冠输赢报表")
@RestController
@RequestMapping("/api/admin/report/hg")
public class HgSportWinReportController {

  @Autowired private HgSportWinReportService hgSportWinReportService;

  @Autowired private GameBetRecordInfoService gameBetRecordInfoService;

  @GetMapping(value = "/findList")
  @PreAuthorize("hasAuthority('report:hg:view')")
  @Operation(summary = "查询皇冠输赢报表")
  public List<HgSportWinReportVO> findList(@Valid HgSportWinReportQueryDTO queryDTO) {
    return hgSportWinReportService.findList(queryDTO);
  }

  @GetMapping(value = "/exportReport")
  @PreAuthorize("hasAuthority('report:hg:export')")
  @Operation(summary = "导出皇冠输赢报表")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'皇冠输赢报表->导出皇冠输赢报表:' + #queryDTO")
  public void exportReport(@Valid HgSportWinReportQueryDTO queryDTO, HttpServletResponse response) {
    hgSportWinReportService.exportReport(queryDTO, response);
  }

  @GetMapping(value = "/findDetail")
  @PreAuthorize("hasAuthority('report:hg:detail')")
  @Operation(summary = "查询皇冠报表注单详情")
  public PageDtoVO<GameBetRecordVO> findDetail(
      Page<GameBetRecordVO> page, GameBetRecordQueryDTO dto) {
    dto.setGameKind(GameKindEnum.HG_SPORT.code());
    dto.setSettle(SettleStatusEnum.YES.getValue().toString());
    dto.setTimeType(TimeTypeEnum.STAT_TIME.getValue());
    return gameBetRecordInfoService.queryPageBetRecord(page, dto);
  }
}
