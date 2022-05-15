package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.GameAmountControlConvert;
import com.gameplat.admin.mapper.GameAmountControlMapper;
import com.gameplat.admin.model.vo.GameAmountControlVO;
import com.gameplat.admin.model.vo.GameAmountNotifyVO;
import com.gameplat.admin.service.GameAmountControlService;
import com.gameplat.common.enums.GameAmountControlTypeEnum;
import com.gameplat.model.entity.game.GameAmountControl;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

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

  @Override
  public GameAmountNotifyVO getGameAmountNotify() {
    GameAmountNotifyVO gameAmountNotifyVO = new GameAmountNotifyVO();
    GameAmountControl gameAmountControl =
        this.lambdaQuery()
            .eq(GameAmountControl::getType, GameAmountControlTypeEnum.LIVE.type())
            .one();
    BigDecimal percentBigDecimal =
        gameAmountControl
            .getUseAmount()
            .divide(gameAmountControl.getAmount(), 4, BigDecimal.ROUND_HALF_UP);
    if (percentBigDecimal
            .multiply(new BigDecimal("100"))
            .compareTo(new BigDecimal(gameAmountControl.getThreshold()))
        >= 0) {
      gameAmountNotifyVO.setAmount(gameAmountControl.getAmount());
      gameAmountNotifyVO.setUseAmount(gameAmountControl.getUseAmount());
      NumberFormat percent = NumberFormat.getPercentInstance();
      percent.setMaximumFractionDigits(4);
      gameAmountNotifyVO.setPercent(percent.format(percentBigDecimal.doubleValue()));
      return gameAmountNotifyVO;
    }
    return null;
  }
}
