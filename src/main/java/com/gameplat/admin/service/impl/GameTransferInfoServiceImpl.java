package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.GameTransferInfoMapper;
import com.gameplat.admin.model.dto.GameRWDataReportDto;
import com.gameplat.admin.service.GameTransferInfoService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.common.enums.TransferTypesEnum;
import com.gameplat.common.game.GameBizBean;
import com.gameplat.common.game.api.GameApi;
import com.gameplat.model.entity.game.GameTransferInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class GameTransferInfoServiceImpl
    extends ServiceImpl<GameTransferInfoMapper, GameTransferInfo>
    implements GameTransferInfoService {

  @Autowired
  private ApplicationContext applicationContext;

  @Resource
  GameTransferInfoMapper gameTransferInfoMapper;

  @Override
  public BigDecimal getAllGameBalance(GameRWDataReportDto dto) {

    return gameTransferInfoMapper.getAllGameBalance(dto);

  }

  @Override
  public List<GameTransferInfo> getGameTransferInfoList(Long memberId) {

    return this.lambdaQuery()
      .eq(ObjectUtils.isNotNull(memberId), GameTransferInfo::getMemberId, memberId)
      .ne(GameTransferInfo::getPlatformCode, TransferTypesEnum.SELF.getCode())
      .list();
  }

  @Override
  public GameTransferInfo getGameTransferInfo(Long memberId, String platformCode) {
    return this.lambdaQuery()
      .eq(ObjectUtils.isNotNull(memberId), GameTransferInfo::getMemberId, memberId)
      .eq(ObjectUtils.isNotNull(platformCode), GameTransferInfo::getPlatformCode, platformCode)
      .ne(GameTransferInfo::getPlatformCode, TransferTypesEnum.SELF.getCode())
      .one();
  }

  /**
   * 同步更新游戏余额
   */
  @Override
  public void insertOrUpdate(GameTransferInfo gameTransferInfo ) {
    gameTransferInfoMapper.saveOrUpdate(gameTransferInfo);
  }

  /**
   * 异步更新游戏余额
   */
  @Override
  @Async
  public void asyncUpdate(GameTransferInfo gameTransferInfo) {
    gameTransferInfoMapper.saveOrUpdate(gameTransferInfo);
  }

  /**
   * 异步查询再更新游戏余额
   */
  @Override
  @Async
  public void asyncQueryAndUpdate(GameTransferInfo gameTransferInfo, GameBizBean gameBizBean) throws Exception {

    GameApi gameApi = getGameApi(gameTransferInfo.getPlatformCode());
    BigDecimal balance = gameApi.getBalance(gameBizBean);
    gameTransferInfo.setLastBalance(balance);
    // 更新游戏余额
    gameTransferInfoMapper.saveOrUpdate(gameTransferInfo);
  }

  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public GameApi getGameApi(String platformCode) {
    GameApi api = applicationContext.getBean(platformCode.toLowerCase() + GameApi.SUFFIX, GameApi.class);
    TransferTypesEnum tt = TransferTypesEnum.get(platformCode);
    // 1代表是否额度转换
    if (tt == null || tt.getType() != 1) {
      throw new ServiceException("游戏未接入");
    }
    return api;
  }
}
