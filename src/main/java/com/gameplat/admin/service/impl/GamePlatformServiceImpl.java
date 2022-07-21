package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.GamePlatformConvert;
import com.gameplat.admin.mapper.GamePlatformMapper;
import com.gameplat.admin.model.dto.GamePlatformQueryDTO;
import com.gameplat.admin.model.dto.OperGamePlatformDTO;
import com.gameplat.admin.service.GameConfigService;
import com.gameplat.admin.service.GamePlatformService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.common.enums.TrueFalse;
import com.gameplat.model.entity.game.GameConfig;
import com.gameplat.model.entity.game.GamePlatform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class GamePlatformServiceImpl extends ServiceImpl<GamePlatformMapper, GamePlatform>
    implements GamePlatformService {

  @Autowired private GamePlatformConvert gamePlatformConvert;

  @Resource private GameConfigService gameConfigService;

  @Override
  public IPage<GamePlatform> selectGamePlatformList(
      PageDTO<GamePlatform> page, GamePlatformQueryDTO dto) {
    // 平台展示跟游戏配置同步
    List<GameConfig> list = gameConfigService.getBaseMapper().selectList(null);
    if (CollectionUtils.isNotEmpty(list)) {
      List<String> codeList =
          list.stream().map(GameConfig::getPlatformCode).collect(Collectors.toList());

      return this.lambdaQuery()
          .eq(
              ObjectUtils.isNotEmpty(dto.getTransfer()),
              GamePlatform::getTransfer,
              dto.getTransfer())
          .eq(
              ObjectUtils.isNotEmpty(dto.getPlatformCode()),
              GamePlatform::getCode,
              dto.getPlatformCode())
          .in(GamePlatform::getCode, codeList)
          .page(page);
    }
    return null;
  }

  @Override
  public void updateGamePlatform(OperGamePlatformDTO operGamePlatformDTO) {
    GamePlatform gamePlatform = gamePlatformConvert.toEntity(operGamePlatformDTO);
    if (!this.updateById(gamePlatform)) {
      throw new ServiceException("更新游戏平台信息失败!");
    }
  }

  @Override
  public List<GamePlatform> queryByTransfer() {
    return Optional.ofNullable(this.query().eq("transfer", TrueFalse.TRUE.getValue()).list())
        .orElse(Collections.emptyList());
  }

  @Override
  public GamePlatform queryByCode(String platformCode) {
    return this.lambdaQuery()
        .eq(ObjectUtils.isNotEmpty(platformCode), GamePlatform::getCode, platformCode)
        .one();
  }
}
