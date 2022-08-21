package com.gameplat.admin.controller.open.game;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.dto.GameBetRecordQueryDTO;
import com.gameplat.admin.model.vo.GameBetRecordVO;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.admin.service.GameBetRecordInfoService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.common.game.GameResult;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@Tag(name = "游戏注单记录")
@Slf4j
@RestController
@RequestMapping("/api/admin/game/gameBetRecord")
public class GameBetRecordController {

  @Autowired private GameBetRecordInfoService gameBetRecordInfoService;

  @Operation(summary = "查询")
  @GetMapping(value = "/queryPage")
  @PreAuthorize("hasAuthority('game:gameBetRecord:view')")
  public PageDtoVO<GameBetRecordVO> queryPage(
      Page<GameBetRecordVO> page, GameBetRecordQueryDTO dto) {
    return gameBetRecordInfoService.queryPageBetRecord(page, dto);
  }

  @SneakyThrows
  @Operation(summary = "查询游戏结果")
  @GetMapping(value = "/getGameResult")
  @PreAuthorize("hasAuthority('game:gameBetRecord:gameResult')")
  public GameResult getGameResult(GameBetRecordQueryDTO dto) {
    return gameBetRecordInfoService.getGameResult(dto);
  }

  @SneakyThrows
  @Operation(summary = "导出游戏下注记录")
  @GetMapping(value = "/exportReport")
  @PreAuthorize("hasAuthority('game:gameBetRecord:export')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'游戏注单记录-->导出游戏下注记录:' + #dto" )
  public void exportReport(GameBetRecordQueryDTO dto, HttpServletResponse response) {
    gameBetRecordInfoService.exportReport(dto, response);
  }
}
