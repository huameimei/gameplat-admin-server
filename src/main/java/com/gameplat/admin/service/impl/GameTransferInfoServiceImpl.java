package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.GameTransferInfoMapper;
import com.gameplat.admin.service.GameTransferInfoService;
import com.gameplat.model.entity.game.GameTransferInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class GameTransferInfoServiceImpl
    extends ServiceImpl<GameTransferInfoMapper, GameTransferInfo>
    implements GameTransferInfoService {


  @Resource
  GameTransferInfoMapper gameTransferInfoMapper;

  @Override
  public List<GameTransferInfo> getGameTransferInfoList(Long memberId) {

    return this.lambdaQuery()
      .eq(ObjectUtils.isNotNull(memberId), GameTransferInfo::getMemberId, memberId)
      .list();
  }

  @Override
  public GameTransferInfo getGameTransferInfo(Long memberId, String platformCode) {
    return this.lambdaQuery()
      .eq(ObjectUtils.isNotNull(memberId), GameTransferInfo::getMemberId, memberId)
      .eq(ObjectUtils.isNotNull(platformCode), GameTransferInfo::getPlatformCode, platformCode)
      .one();
  }

  @Override
  public void insertOrUpdate(GameTransferInfo gameTransferInfo ) {
    gameTransferInfoMapper.saveOrUpdate(gameTransferInfo);
  }
}
