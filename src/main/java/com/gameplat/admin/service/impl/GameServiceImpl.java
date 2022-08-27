package com.gameplat.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.GameConvert;
import com.gameplat.admin.mapper.GameMapper;
import com.gameplat.admin.model.dto.GameQueryDTO;
import com.gameplat.admin.model.dto.OperGameDTO;
import com.gameplat.admin.model.vo.GameVO;
import com.gameplat.admin.service.GameKindService;
import com.gameplat.admin.service.GamePlatformService;
import com.gameplat.admin.service.GameService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.DateUtil;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.model.entity.game.Game;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gameplat.model.entity.game.GameKind;
import com.gameplat.model.entity.game.GamePlatform;
import org.apache.commons.collections.CollectionUtils;
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
  public IPage<GameVO> selectGameList(PageDTO<Game> page, GameQueryDTO dto) {
    return this.lambdaQuery()
        .like(ObjectUtils.isNotEmpty(dto.getGameName()), Game::getGameName, dto.getGameName())
        .eq(
            ObjectUtils.isNotEmpty(dto.getPlatformCode()),
            Game::getPlatformCode,
            dto.getPlatformCode())
        .eq(ObjectUtils.isNotEmpty(dto.getGameType()), Game::getGameType, dto.getGameType())
        .eq(ObjectUtils.isNotEmpty(dto.getGameKind()), Game::getGameKind, dto.getGameKind())
        .eq(ObjectUtils.isNotEmpty(dto.getIsH5()), Game::getIsH5, dto.getIsH5())
        .eq(ObjectUtils.isNotEmpty(dto.getIsPc()), Game::getIsPc, dto.getIsPc())
        .page(page)
        .convert(gameConvert::toVo);
  }

  @Override
  public void updateGame(OperGameDTO operGameDTO) {
    Game game = gameConvert.toEntity(operGameDTO);
    if (!this.updateById(game)) {
      throw new ServiceException("更新游戏信息失败!");
    }
  }

  @Autowired private GamePlatformService gamePlatformService;

  @Autowired private GameKindService gameKindService;

  @Override
  public boolean checkTransferMainTime(String platformCode) {
    GamePlatform gamePlatform = gamePlatformService.queryByCode(platformCode);
    List<GameKind> gameKindList = gameKindService.queryGameKindListByPlatformCode(platformCode);
    if (ObjectUtil.isNull(gamePlatform) || CollectionUtil.isEmpty(gameKindList)) {
      return false;
    }

    return gameKindList.stream()
      .filter(e -> gamePlatform.getCode().equals(e.getPlatformCode()))
      .anyMatch(this::isGameMaintain);
  }

  private boolean isGameMaintain(GameKind gameKind) {
    return this.isGameMaintain(gameKind, new Date());
  }

  private boolean isGameMaintain(GameKind gameKind, Date date) {
    Date start = gameKind.getMaintenanceTimeStart();
    Date end = gameKind.getMaintenanceTimeEnd();

    return ObjectUtils.isNotEmpty(start)
      && ObjectUtils.isNotEmpty(end)
      && DateUtil.dateCompareByYmdhms(date, start, end);
  }

  @Override
  public List<GameVO> findByGameTypeCode(String gameTypeCode) {
    List<Game> list =
        this.lambdaQuery()
            .eq(StringUtils.isNotBlank(gameTypeCode), Game::getGameType, gameTypeCode)
            .list();
    List<GameVO> gameVOList = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(list)) {
      for (Game game : list) {
        gameVOList.add(gameConvert.toVo(game));
      }
    }
    return gameVOList;
  }
}
