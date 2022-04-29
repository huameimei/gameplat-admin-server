package com.gameplat.admin.controller.open.game;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.dto.GameBetRecordQueryDTO;
import com.gameplat.admin.model.vo.GameBetRecordVO;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.admin.service.GameBetRecordInfoService;
import com.gameplat.common.game.GameResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "游戏注单记录")
@Slf4j
@RestController
@RequestMapping("/api/admin/game/gameBetRecord")
public class GameBetRecordController {

  @Autowired private GameBetRecordInfoService gameBetRecordInfoService;

  @SneakyThrows
  @ApiOperation("查询")
  @GetMapping(value = "/queryPage")
  @PreAuthorize("hasAuthority('game:gameBetRecord:view')")
  public PageDtoVO<GameBetRecordVO> queryPage(
      Page<GameBetRecordVO> page, GameBetRecordQueryDTO dto) {
    return gameBetRecordInfoService.queryPageBetRecord(page, dto);
  }

  @SneakyThrows
  @ApiOperation("查询游戏结果")
  @GetMapping(value = "/getGameResult")
  @PreAuthorize("hasAuthority('game:gameBetRecord:gameResult')")
  public GameResult getGameResult(GameBetRecordQueryDTO dto) {
    return gameBetRecordInfoService.getGameResult(dto);
  }
}
