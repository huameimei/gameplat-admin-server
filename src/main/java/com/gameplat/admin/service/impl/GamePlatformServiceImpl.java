package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.GamePlatformConvert;
import com.gameplat.admin.mapper.GamePlatformMapper;
import com.gameplat.admin.model.domain.GamePlatform;
import com.gameplat.admin.model.dto.GamePlatformQueryDTO;
import com.gameplat.admin.model.dto.OperGamePlatformDTO;
import com.gameplat.admin.service.GamePlatformService;
import com.gameplat.base.common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class GamePlatformServiceImpl extends ServiceImpl<GamePlatformMapper, GamePlatform>
    implements GamePlatformService {

  @Autowired private GamePlatformConvert gamePlatformConvert;

  @Override
  public IPage<GamePlatform> selectGamePlatformList(
      PageDTO<GamePlatform> page, GamePlatformQueryDTO dto) {
    return this.lambdaQuery()
        .eq(ObjectUtils.isNotEmpty(dto.getTransfer()), GamePlatform::getTransfer, dto.getTransfer())
        .eq(
            ObjectUtils.isNotEmpty(dto.getPlatformCode()),
            GamePlatform::getCode,
            dto.getPlatformCode())
        .page(page);
  }

  @Override
  public void updateGamePlatform(OperGamePlatformDTO operGamePlatformDTO) {
    GamePlatform gamePlatform = gamePlatformConvert.toEntity(operGamePlatformDTO);
    if (!this.updateById(gamePlatform)) {
      throw new ServiceException("更新游戏平台信息失败!");
    }
  }
}
