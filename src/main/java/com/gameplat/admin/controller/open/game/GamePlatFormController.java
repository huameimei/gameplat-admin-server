package com.gameplat.admin.controller.open.game;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.GamePlatformQueryDTO;
import com.gameplat.admin.model.dto.OperGamePlatformDTO;
import com.gameplat.admin.service.GamePlatformService;
import com.gameplat.common.lang.Assert;
import com.gameplat.model.entity.game.GamePlatform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/game/gamePlatform")
public class GamePlatFormController {

  @Autowired private GamePlatformService gamePlatformService;

  @GetMapping("/list")
  @PreAuthorize("hasAuthority('game:gamePlatform:view')")
  public IPage<GamePlatform> selectGameList(PageDTO<GamePlatform> page, GamePlatformQueryDTO dto) {
    return gamePlatformService.selectGamePlatformList(page, dto);
  }

  @PutMapping("/edit")
  @PreAuthorize("hasAuthority('game:gamePlatform:edit')")
  public void updateLiveGame(@RequestBody OperGamePlatformDTO operGamePlatformDTO) {
    Assert.notNull(operGamePlatformDTO.getId(), "缺少参数");
    Assert.notEmpty(operGamePlatformDTO.getCode(), "缺少编码参数");
    gamePlatformService.updateGamePlatform(operGamePlatformDTO);
  }

  /** 获取所有的真人平台 */
  @GetMapping(value = "/queryGamePlatformList")
  public List<GamePlatform> queryGamePlatformList() {
    return Optional.ofNullable(gamePlatformService.query().list()).orElse(Collections.emptyList());
  }

  /** 获取所有可以额度开启的 */
  @GetMapping(value = "/queryByTransfer")
  public List<GamePlatform> queryByTransfer() {
    return gamePlatformService.queryByTransfer();
  }
}
