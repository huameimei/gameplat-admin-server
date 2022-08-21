package com.gameplat.admin.controller.open.game;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.GameKindQueryDTO;
import com.gameplat.admin.model.dto.OperGameKindDTO;
import com.gameplat.admin.model.vo.GameKindVO;
import com.gameplat.admin.service.GameKindService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.common.enums.GameDemoEnableEnum;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.game.GameKind;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Tag(name = "游戏分类")
@RestController
@RequestMapping("/api/admin/game/gameKind")
public class GameKindController {

  @Autowired private GameKindService gameKindService;

  @Operation(summary = "查询")
  @GetMapping("/list")
  @PreAuthorize("hasAuthority('game:gameKind:view')")
  public IPage<GameKindVO> selectGameKindList(PageDTO<GameKind> page, GameKindQueryDTO dto) {
    return gameKindService.selectGameKindList(page, dto);
  }

  @Operation(summary = "编辑")
  @PostMapping("/edit")
  @PreAuthorize("hasAuthority('game:gameKind:edit')")
  @Log(
          module = ServiceName.ADMIN_SERVICE,
          type = LogType.LIVEREBATE,
          desc = "'编辑保存游戏:最新返水比例为:' + #rebateRate + ',最新打码计算成长值倍数为:' + #damaRate")
  public void updateGameKind(@RequestBody OperGameKindDTO dto) {
    gameKindService.updateGameKind(dto);
  }

  @Operation(summary = "游戏开关")
  @PostMapping("/updateEnable")
  @PreAuthorize("hasAuthority('game:gameKind:updateEnable')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'游戏分类-->游戏开关:' + #dto" )
  public void updateEnable(@RequestBody OperGameKindDTO dto) {
    gameKindService.updateEnable(dto);
  }

  @Operation(summary = "试玩开关")
  @PostMapping("/updateDemoEnable")
  @PreAuthorize("hasAuthority('game:gameKind:updateDemoEnable')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'游戏分类-->试玩开关:' + #dto" )
  public void updateDemoEnable(@RequestBody OperGameKindDTO dto) {
    if (dto.getDemoEnable() != GameDemoEnableEnum.ENABLE.getCode()
        && dto.getDemoEnable() != GameDemoEnableEnum.DISABLE.getCode()) {
      throw new ServiceException("参数错误");
    }
    gameKindService.updateDemoEnable(dto);
  }

  @Operation(summary = "获取全部")
  @GetMapping(value = "/queryGameKindList")
  @PreAuthorize("hasAuthority('game:gameKind:view')")
  public List<GameKind> queryGameKindList() {
    return Optional.ofNullable(gameKindService.queryUnDownGameKindList()).orElse(Collections.emptyList());
  }
}
