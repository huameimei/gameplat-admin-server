package com.gameplat.admin.controller.open.game;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.dto.GameRebateReportQueryDTO;
import com.gameplat.admin.model.dto.GameRebateStatisQueryDTO;
import com.gameplat.admin.model.dto.OperGameRebateDetailDTO;
import com.gameplat.admin.model.vo.GameReportVO;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.admin.service.GameRebateReportService;
import com.gameplat.model.entity.game.GameRebateDetail;
import com.gameplat.model.entity.game.GameRebateReport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "游戏返水报表")
@Slf4j
@RestController
@RequestMapping("/api/admin/game/gameRebateReport")
public class GameRebateReportController {

  @Autowired private GameRebateReportService gameRebateReportService;

  @ApiOperation("查询")
  @GetMapping(value = "/queryPage")
  @PreAuthorize("hasAuthority('game:gameRebateReport:view')")
  public PageDtoVO<GameRebateDetail> queryPage(
      Page<GameRebateDetail> page, GameRebateReportQueryDTO dto) {
    return gameRebateReportService.queryPage(page, dto);
  }

  @ApiOperation("游戏返点拒发")
  @PostMapping(value = "/reject")
  @PreAuthorize("hasAuthority('game:gameRebateReport:reject')")
  public void reject(@RequestBody OperGameRebateDetailDTO dto) {
    gameRebateReportService.reject(dto.getAccount(), dto.getPeriodName(), dto.getRemark());
  }

  @ApiOperation("游戏返点更改")
  @PostMapping(value = "/modify")
  @PreAuthorize("hasAuthority('game:gameRebateReport:modify')")
  public void modify(@RequestBody OperGameRebateDetailDTO dto) {
    gameRebateReportService.modify(dto.getId(), dto.getRealRebateMoney(), dto.getRemark());
  }

  @ApiOperation("游戏返点明细查询")
  @GetMapping(value = "/queryDetail")
  @PreAuthorize("hasAuthority('game:gameRebateReport:queryDetail')")
  public List<GameRebateReport> queryDetail(GameRebateReportQueryDTO dto) {
    return gameRebateReportService.queryDetail(dto);
  }

  @ApiOperation("游戏交收数据统计查询")
  @GetMapping(value = "/queryReport")
  @PreAuthorize("hasAuthority('game:gameRebateReport:list')")
  public List<GameReportVO> queryGameReport(GameRebateStatisQueryDTO dto) {
    return gameRebateReportService.queryGameReport(dto);
  }
}
