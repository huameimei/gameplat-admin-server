package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.GameKindConvert;
import com.gameplat.admin.mapper.GameKindMapper;
import com.gameplat.admin.model.domain.GameKind;
import com.gameplat.admin.model.dto.GameKindQueryDTO;
import com.gameplat.admin.model.dto.OperGameKindDTO;
import com.gameplat.admin.service.GameKindService;
import com.gameplat.common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class GameKindServiceImpl extends ServiceImpl<GameKindMapper, GameKind>
    implements GameKindService {

  @Autowired private GameKindConvert gameKindConvert;

  @Override
  public IPage<GameKind> selectGameKindList(PageDTO<GameKind> page, GameKindQueryDTO dto) {
    return this.lambdaQuery()
        .eq(ObjectUtils.isNotEmpty(dto.getGameType()), GameKind::getGameType, dto.getGameType())
        .eq(
            ObjectUtils.isNotEmpty(dto.getPlatformCode()),
            GameKind::getPlatformCode,
            dto.getPlatformCode())
        .page(page);
  }

  @Override
  public void updateGameKind(OperGameKindDTO operGameKindDTO) {
    GameKind gameKind = gameKindConvert.toEntity(operGameKindDTO);
    if (!this.updateById(gameKind)) {
      throw new ServiceException("更新游戏分类信息失败!");
    }
  }
}
