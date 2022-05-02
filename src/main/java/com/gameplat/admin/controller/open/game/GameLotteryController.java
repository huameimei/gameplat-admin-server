package com.gameplat.admin.controller.open.game;

import com.gameplat.admin.model.vo.GameLotteryVo;
import com.gameplat.admin.service.GameLotteryMapperService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "游戏彩票")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/game/gameLottry")
public class GameLotteryController {

  private final GameLotteryMapperService gameLotteryMapperService;

  @ApiOperation("获取彩票类型")
  @GetMapping("findGameLotteryType/{code}")
  public List<GameLotteryVo> findGameLotteryType(@PathVariable("code") int code) {
    return gameLotteryMapperService.findGameLotteryType(code);
  }
}
