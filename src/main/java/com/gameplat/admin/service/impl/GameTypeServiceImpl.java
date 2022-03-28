package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.GameTypeConvert;
import com.gameplat.admin.mapper.GameTypeMapper;
import com.gameplat.admin.model.dto.GameTypeQueryDTO;
import com.gameplat.admin.model.dto.OperGameTypeDTO;
import com.gameplat.admin.model.vo.GameTypeVO;
import com.gameplat.admin.service.GameTypeService;
import com.gameplat.common.lang.Assert;
import com.gameplat.model.entity.game.GameType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class GameTypeServiceImpl extends ServiceImpl<GameTypeMapper, GameType>
    implements GameTypeService {

  @Autowired private GameTypeConvert gameTypeConvert;

  @Override
  public IPage<GameTypeVO> selectGameTypeList(PageDTO<GameType> page, GameTypeQueryDTO dto) {
    return this.lambdaQuery()
        .eq(
            ObjectUtils.isNotEmpty(dto.getGameTypeCode()),
            GameType::getGameTypeCode,
            dto.getGameTypeCode())
        .eq(ObjectUtils.isNotEmpty(dto.getStatus()), GameType::getStatus, dto.getStatus())
        .page(page)
        .convert(gameTypeConvert::toVo);
  }

  @Override
  public void updateGameType(OperGameTypeDTO dto) {
    GameType gameType = gameTypeConvert.toEntity(dto);
    Assert.isTrue(this.updateById(gameType), "更新游戏大类信息失败!");
  }
}
