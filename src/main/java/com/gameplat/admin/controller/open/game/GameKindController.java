package com.gameplat.admin.controller.open.game;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.GameKindQueryDTO;
import com.gameplat.admin.model.dto.OperGameKindDTO;
import com.gameplat.admin.model.vo.GameKindVO;
import com.gameplat.admin.service.GameKindService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.common.enums.GameDemoEnableEnum;
import com.gameplat.model.entity.game.GameKind;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Api(tags = "游戏分类")
@RestController
@RequestMapping("/api/admin/game/gameKind")
public class GameKindController {

  @Autowired private GameKindService gameKindService;

  @ApiOperation("查询")
  @GetMapping("/list")
  @PreAuthorize("hasAuthority('game:gameKind:view')")
  public IPage<GameKindVO> selectGameKindList(PageDTO<GameKind> page, GameKindQueryDTO dto) {
    return gameKindService.selectGameKindList(page, dto);
  }

  @ApiOperation("编辑")
  @PutMapping("/edit")
  @PreAuthorize("hasAuthority('game:gameKind:edit')")
  public void updateGameKind(@RequestBody OperGameKindDTO dto) {
    gameKindService.updateGameKind(dto);
  }

  @ApiOperation("游戏开关")
  @PutMapping("/updateEnable")
  @PreAuthorize("hasAuthority('game:gameKind:updateEnable')")
  public void updateEnable(@RequestBody OperGameKindDTO dto) {
    gameKindService.updateEnable(dto);
  }

  @ApiOperation("试玩开关")
  @PutMapping("/updateDemoEnable")
  @PreAuthorize("hasAuthority('game:gameKind:updateDemoEnable')")
  public void updateDemoEnable(@RequestBody OperGameKindDTO dto) {
    if (dto.getDemoEnable() != GameDemoEnableEnum.ENABLE.getCode()
        && dto.getDemoEnable() != GameDemoEnableEnum.DISABLE.getCode()) {
      throw new ServiceException("参数错误");
    }
    gameKindService.updateDemoEnable(dto);
  }

  @ApiOperation("获取全部")
  @GetMapping(value = "/queryGameKindList")
  @PreAuthorize("hasAuthority('game:gameKind:view')")
  public List<GameKind> queryGameKindList() {
    return Optional.ofNullable(gameKindService.query().list()).orElse(Collections.emptyList());
  }
}
