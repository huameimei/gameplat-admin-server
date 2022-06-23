package com.gameplat.admin.controller.open.game;

import com.gameplat.admin.model.vo.GameAmountControlVO;
import com.gameplat.admin.model.vo.GameAmountNotifyVO;
import com.gameplat.admin.service.GameAmountControlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "游戏额度控制")
@Slf4j
@RestController
@RequestMapping("/api/admin/game/gameAmountControl")
public class GameAmountControlController {

  @Autowired private GameAmountControlService gameAmountControlService;

  @Operation(summary = "查询")
  @GetMapping("/list")
  @PreAuthorize("hasAuthority('game:gameAmountControl:list')")
  public List<GameAmountControlVO> selectGameAmountList() {
    return gameAmountControlService.selectGameAmountList();
  }

  @GetMapping("/notifyInfo")
  public GameAmountNotifyVO getGameAmountNotify() {
    return gameAmountControlService.getGameAmountNotify();
  }
}
