package com.gameplat.admin.controller.open.game;

import com.gameplat.admin.service.KgNlLotteryMapService;
import com.gameplat.model.entity.game.KgNlLotteryMap;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author aBen
 * @date 2022/8/11 21:42
 * @desc
 */
@Tag(name = "游戏类型")
@RestController
@RequestMapping("/api/admin/game/map/kgNl")
public class KgNlLotteryMapController {

  @Autowired
  private KgNlLotteryMapService kgNlLotteryMapService;

  @Operation(summary = "查询")
  @GetMapping("/findMapList")
  public List<KgNlLotteryMap> findMapList() {
    return kgNlLotteryMapService.findMapList();
  }
}
