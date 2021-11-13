package com.gameplat.admin.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.GameConvert;
import com.gameplat.admin.mapper.GameMapper;
import com.gameplat.admin.model.domain.Game;
import com.gameplat.admin.model.dto.GameQueryDTO;
import com.gameplat.admin.model.dto.OperGameDTO;
import com.gameplat.admin.service.GameService;
import com.gameplat.common.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class GameServiceImpl extends ServiceImpl<GameMapper, Game> implements GameService {

  @Autowired private GameConvert gameConvert;

  @Override
  @SentinelResource(value = "selectGameList")
  public IPage<Game> selectGameList(PageDTO<Game> page, GameQueryDTO dto) {
    return this.lambdaQuery()
        .like(ObjectUtils.isNotEmpty(dto.getGameName()), Game::getChineseName, dto.getGameName())
        .eq(
            ObjectUtils.isNotEmpty(dto.getPlatformCode()),
            Game::getPlatformCode,
            dto.getPlatformCode())
        .eq(ObjectUtils.isNotEmpty(dto.getGameType()), Game::getGameType, dto.getGameType())
        .eq(ObjectUtils.isNotEmpty(dto.getIsH5()), Game::getIsH5, dto.getIsH5())
        .eq(ObjectUtils.isNotEmpty(dto.getIsFlash()), Game::getIsFlash, dto.getIsFlash())
        .page(page);
  }

  @Override
  public void updateGame(OperGameDTO operGameDTO) {
    Game game = gameConvert.toEntity(operGameDTO);
    if (this.updateById(game)) {
      throw new ServiceException("更新游戏信息失败!");
    }
  }
}
