package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.GameAmountControlConvert;
import com.gameplat.admin.mapper.GameAmountControlMapper;
import com.gameplat.admin.model.vo.GameAmountControlVO;
import com.gameplat.admin.service.GameAmountControlService;
import com.gameplat.model.entity.game.GameAmountControl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class GameAmountControlServiceImpl
    extends ServiceImpl<GameAmountControlMapper, GameAmountControl>
    implements GameAmountControlService {

  private final GameAmountControlConvert gameAmountControlConvert;

  @Override
  public List<GameAmountControlVO> selectGameAmountList() {
    return this.lambdaQuery().list().stream()
        .map(gameAmountControlConvert::toVo)
        .collect(Collectors.toList());
  }

  @Override
  public GameAmountControl findInfoByType(Integer type) {
    return this.lambdaQuery().eq(GameAmountControl::getType, type).one();
  }
}
