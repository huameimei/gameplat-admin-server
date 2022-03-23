package com.gameplat.admin.controller.open.game;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.dto.GameRebateReportQueryDTO;
import com.gameplat.admin.model.dto.OperGameRebateDetailDTO;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.admin.service.GameRebateReportService;
import com.gameplat.model.entity.game.GameRebateDetail;
import com.gameplat.model.entity.game.GameRebateReport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/admin/game/gameRebateReport")
public class GameRebateReportController {

  @Autowired private GameRebateReportService gameRebateReportService;

  @GetMapping(value = "/queryPage")
  public PageDtoVO<GameRebateDetail> queryPage(
      Page<GameRebateDetail> page, GameRebateReportQueryDTO dto) {
    return gameRebateReportService.queryPage(page, dto);
  }

  @PostMapping(value = "/reject")
  public void reject(@RequestBody OperGameRebateDetailDTO dto) {
    gameRebateReportService.reject(dto.getAccount(), dto.getPeriodName(), dto.getRemark());
  }

  @PutMapping(value = "/modify")
  public void modify(@RequestBody OperGameRebateDetailDTO dto) {
    gameRebateReportService.modify(dto.getId(), dto.getRealRebateMoney(), dto.getRemark());
  }

  @GetMapping(value = "/queryDetail")
  public List<GameRebateReport> queryDetail(GameRebateReportQueryDTO dto) {
    return gameRebateReportService.queryDetail(dto);
  }

  // TODO 真人返点统计导出

}
