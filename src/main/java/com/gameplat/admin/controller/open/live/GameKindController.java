package com.gameplat.admin.controller.open.live;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.GameKind;
import com.gameplat.admin.model.dto.GameKindQueryDTO;
import com.gameplat.admin.model.dto.OperGameKindDTO;
import com.gameplat.admin.model.vo.GameKindVO;
import com.gameplat.admin.service.GameKindService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.common.enums.LiveDemoEnableEnum;
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
@RequestMapping("/api/admin/live/gameKind")
public class GameKindController {

  @Autowired private GameKindService gameKindService;

  @GetMapping("/list")
  @PreAuthorize("hasAuthority('live:gameKind:list')")
  public IPage<GameKindVO> selectGameKindList(PageDTO<GameKind> page, GameKindQueryDTO dto) {
    return gameKindService.selectGameKindList(page, dto);
  }

  @PutMapping("/edit")
  @PreAuthorize("hasAuthority('live:gameKind:edit')")
  public void updateGameKind(@RequestBody OperGameKindDTO operGameKindDTO) {
    gameKindService.updateGameKind(operGameKindDTO);
  }

  @PutMapping("/updateEnable")
  public void updateEnable(@RequestBody OperGameKindDTO operGameKindDTO) {
    try {
      gameKindService.updateEnable(operGameKindDTO);
    } catch (Exception e) {
      throw new ServiceException("更新所有真人子游戏失败！");
    }
  }

  @PutMapping("/updateDemoEnable")
  public void updateDemoEnable(@RequestBody OperGameKindDTO operGameKindDTO) {
    if(operGameKindDTO.getDemoEnable() != LiveDemoEnableEnum.ENABLE.getCode()
        && operGameKindDTO.getDemoEnable()!= LiveDemoEnableEnum.DISABLE.getCode() ) {
      throw new ServiceException("参数错误");
    }
    try{
      gameKindService.updateDemoEnable(operGameKindDTO);
    }catch (Exception e){
      throw new ServiceException("所有免费真人子游戏（支持试玩的）！");
    }
  }

  @GetMapping(value = "queryLiveGameKindList")
  public List<GameKind> queryLiveGameKindList() {
    return Optional.ofNullable(gameKindService.query().list()).orElse(Collections.emptyList());
  }
}
