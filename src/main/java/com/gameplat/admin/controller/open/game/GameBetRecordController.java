package com.gameplat.admin.controller.open.game;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.dto.GameBetRecordQueryDTO;
import com.gameplat.admin.model.vo.GameBetRecordVO;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.admin.service.GameBetRecordInfoService;
import com.gameplat.common.game.GameResult;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/admin/game/gameBetRecord")
public class GameBetRecordController {

  @Autowired private GameBetRecordInfoService gameBetRecordInfoService;

  @GetMapping(value = "/queryPage")
  public PageDtoVO<GameBetRecordVO> queryPage(Page<GameBetRecordVO> page, GameBetRecordQueryDTO dto)
      throws Exception {
    return gameBetRecordInfoService.queryPageBetRecord(page, dto);
  }

  @GetMapping(value = "/getGameResult")
  public GameResult getGameResult(GameBetRecordQueryDTO dto, HttpServletResponse response)
      throws Exception {
    return gameBetRecordInfoService.getGameResult(dto);
  }
}
