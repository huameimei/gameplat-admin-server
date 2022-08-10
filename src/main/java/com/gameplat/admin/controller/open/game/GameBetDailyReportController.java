package com.gameplat.admin.controller.open.game;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.dto.GameBetDailyReportQueryDTO;
import com.gameplat.admin.model.dto.GameBetRecordQueryDTO;
import com.gameplat.admin.model.dto.OperGameMemberDayReportDTO;
import com.gameplat.admin.model.dto.UserGameBetRecordDto;
import com.gameplat.admin.model.vo.GameBetRecordVO;
import com.gameplat.admin.model.vo.GameBetReportVO;
import com.gameplat.admin.model.vo.GameReportVO;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.admin.service.GameBetDailyReportService;
import com.gameplat.admin.service.GameBetRecordInfoService;
import com.gameplat.admin.service.GamePlatformService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.DateUtil;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.model.entity.game.GameBetDailyReport;
import com.gameplat.model.entity.game.GamePlatform;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Slf4j
@Tag(name = "游戏日报表")
@RestController
@RequestMapping("/api/admin/game/gameBetDailyReport")
public class GameBetDailyReportController {

  @Autowired private GameBetDailyReportService gameBetDailyReportService;

  @Autowired private GamePlatformService gamePlatformService;

  @Autowired private GameBetRecordInfoService gameBetRecordInfoService;

  @Operation(summary = "查询")
  @GetMapping(value = "/queryPage")
  @PreAuthorize("hasAuthority('game:gameBetDailyReport:view')")
  public PageDtoVO<GameBetDailyReport> queryPage(
      Page<GameBetDailyReport> page, GameBetDailyReportQueryDTO dto) {
    return gameBetDailyReportService.queryPage(page, dto);
  }

  @Operation(summary = "游戏投注日报表导出")
  @GetMapping(value = "/exportGameBetDailyReport")
  @PreAuthorize("hasAuthority('game:gameBetDailyReport:export')")
  public void exportGameBetDailyReport(HttpServletResponse response, GameBetDailyReportQueryDTO dto) throws Exception {
    gameBetDailyReportService.exportGameBetDailyReport(response, dto);
  }

  @Operation(summary = "游戏平台维度数据统计")
  @GetMapping(value = "/queryGamePlatformReport")
  @PreAuthorize("hasAuthority('game:gamePlatformReport:view')")
  public List<GameReportVO> queryGamePlatformReport(GameBetDailyReportQueryDTO dto) {
    return gameBetDailyReportService.queryGamePlatformReport(dto);
  }

  @Operation(summary = "游戏平台维度数据导出")
  @GetMapping(value = "/exportGamePlatformReport")
  @PreAuthorize("hasAuthority('game:gamePlatformReport:export')")
  public void exportGamePlatformReport(
      GameBetDailyReportQueryDTO dto, HttpServletResponse response) {
    gameBetDailyReportService.exportGamePlatformReport(dto, response);
  }

  @Operation(summary = "游戏重新生成日报表")
  @PostMapping(value = "/resetDayReport")
  @PreAuthorize("hasAuthority('game:gameBetDailyReport:reset')")
  public void resetDayReport(@RequestBody OperGameMemberDayReportDTO dto) {
    List<GamePlatform> list = gamePlatformService.list();
    GamePlatform gamePlatform =
        list.stream()
            .filter(item -> item.getCode().equals(dto.getPlatformCode()))
            .findAny()
            .orElseThrow(() -> new ServiceException("游戏类型不存在！"));
    gameBetDailyReportService.saveGameBetDailyReport(dto.getStatTime(), gamePlatform);
  }

  @Operation(summary = "查询游戏数据统计")
  @GetMapping(value = "/queryReport")
  @PreAuthorize("hasAuthority('game:gameReport:view')")
  public List<GameReportVO> queryReport(GameBetDailyReportQueryDTO dto) {
    if (StringUtils.isBlank(dto.getBeginTime())) {
      String beginTime = DateUtil.getDateToString(new Date());
      dto.setBeginTime(beginTime);
    }
    if (StringUtils.isBlank(dto.getEndTime())) {
      String endTime = DateUtil.getDateToString(new Date());
      dto.setEndTime(endTime);
    }
    return gameBetDailyReportService.queryReportList(dto);
  }

  @Operation(summary = "游戏大类数据导出")
  @GetMapping(value = "/exportGameKindReport")
  @PreAuthorize("hasAuthority('game:gameKindReport:export')")
  public void exportGameKindReport(GameBetDailyReportQueryDTO dto, HttpServletResponse response) {
    if (StringUtils.isBlank(dto.getBeginTime())) {
      String beginTime = DateUtil.getDateToString(new Date());
      dto.setBeginTime(beginTime);
    }
    if (StringUtils.isBlank(dto.getEndTime())) {
      String endTime = DateUtil.getDateToString(new Date());
      dto.setEndTime(endTime);
    }
    gameBetDailyReportService.exportGameKindReport(dto, response);
  }

  @Operation(summary = "查询投注日报表记录")
  @GetMapping(value = "/queryBetReport")
  public PageDtoVO<GameBetReportVO> queryBetReport(
      Page<GameBetDailyReportQueryDTO> page, GameBetDailyReportQueryDTO dto) {
    if (StringUtils.isEmpty(dto.getBeginTime())) {
      String beginTime = DateUtil.getDateToString(new Date());
      dto.setBeginTime(beginTime);
    }
    if (StringUtils.isEmpty(dto.getEndTime())) {
      String endTime = DateUtil.getDateToString(new Date());
      dto.setEndTime(endTime);
    }
    if (StringUtils.isNotEmpty(dto.getGameType())) {
      StringBuilder sb = new StringBuilder();
      sb.append("(");
      String[] split = dto.getGameType().split(",");
      for (String s : split) {
        sb.append("'" + s + "'").append(",");
      }
      sb.deleteCharAt(sb.toString().length() - 1);
      sb.append(")");
      String str = sb.toString();
      dto.setGameType(str);
    }

    if (StringUtils.isNotEmpty(dto.getPlatformCode())) {
      dto.setGameType("");
      StringBuilder sb = new StringBuilder();
      sb.append("(");
      String[] split = dto.getPlatformCode().split(",");
      for (String s : split) {
        sb.append("'").append(s).append("'").append(",");
      }
      sb.deleteCharAt(sb.toString().length() - 1);
      sb.append(")");
      String str = sb.toString();
      dto.setPlatformCode(str);
    }

    return gameBetDailyReportService.queryBetReportList(page, dto);
  }

  @Operation(summary = "获取会员投注记录")
  @GetMapping("findUserGameBetRecord")
  @PreAuthorize("hasAuthority('game:gameReport:view')")
  public PageDtoVO<GameBetRecordVO> findUserGameBetRecord(
      Page<GameBetRecordVO> page, @Valid UserGameBetRecordDto dto) {
    GameBetRecordQueryDTO gameBetRecordQueryDTO = new GameBetRecordQueryDTO();
    BeanUtil.copyProperties(dto, gameBetRecordQueryDTO);
    gameBetRecordQueryDTO.setTimeType(1);
    return gameBetRecordInfoService.queryPageBetRecord(page, gameBetRecordQueryDTO);
  }

  @Operation(summary = "导出会员游戏报表")
  @GetMapping("exportUserGameBetRecord")
  @PreAuthorize("hasAuthority('game:gameReport:export')")
  public void exportUserGameBetRecord(@Valid UserGameBetRecordDto dto, HttpServletResponse response) throws IOException {
    GameBetRecordQueryDTO gameBetRecordQueryDTO = new GameBetRecordQueryDTO();
    BeanUtils.copyProperties(dto, gameBetRecordQueryDTO);
    gameBetRecordQueryDTO.setTimeType(1);
    gameBetRecordInfoService.exportUserGameBetRecord(gameBetRecordQueryDTO, response);
  }

}
