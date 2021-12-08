package com.gameplat.admin.controller.open.live;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.GamePlatform;
import com.gameplat.admin.model.dto.GamePlatformQueryDTO;
import com.gameplat.admin.model.dto.OperGamePlatformDTO;
import com.gameplat.admin.service.GamePlatformService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/live/gamePlatform/")
public class GamePlatFormController {

  @Autowired private GamePlatformService gamePlatformService;

  @GetMapping("/list")
  @PreAuthorize("hasAuthority('live:gamePlatform:list')")
  public IPage<GamePlatform> selectGameList(PageDTO<GamePlatform> page, GamePlatformQueryDTO dto) {
    return gamePlatformService.selectGamePlatformList(page, dto);
  }

  @PutMapping("/edit")
  @PreAuthorize("hasAuthority('live:gamePlatform:edit')")
  public void updateLiveGame(@RequestBody OperGamePlatformDTO operGamePlatformDTO) {
    if (StringUtils.isNull(operGamePlatformDTO.getId())) {
      throw new ServiceException("缺少参数");
    }
    if (StringUtils.isBlank(operGamePlatformDTO.getCode())) {
      throw new ServiceException("缺少编码参数");
    }
    gamePlatformService.updateGamePlatform(operGamePlatformDTO);
  }

  /** 获取所有的真人平台 */
  @GetMapping(value = "queryLiveList")
  public List<GamePlatform> queryLiveList() {
    return Optional.ofNullable(gamePlatformService.query().list()).orElse(Collections.emptyList());
  }

  /**
   * 获取所有可以额度开启的
   */
  @GetMapping(value = "queryByTransfer")
  public List<GamePlatform> queryByTransfer() {
    return gamePlatformService.queryByTransfer();
  }
}
