package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.GameTransferInfoMapper;
import com.gameplat.admin.service.GameTransferInfoService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.model.entity.game.GameTransferInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class GameTransferInfoServiceImpl
    extends ServiceImpl<GameTransferInfoMapper, GameTransferInfo>
    implements GameTransferInfoService {

  @Override
  public GameTransferInfo getInfoByMemberId(Long memberId) {
    return this.lambdaQuery()
        .eq(ObjectUtils.isNotNull(memberId), GameTransferInfo::getMemberId, memberId)
        .one();
  }

  @Override
  public void update(Long memberId, String code, String orderNo) {
    LambdaUpdateWrapper<GameTransferInfo> lambdaUpdateWrapper = new LambdaUpdateWrapper();
    lambdaUpdateWrapper
        .eq(GameTransferInfo::getMemberId, memberId)
        .set(GameTransferInfo::getPlatformCode, code)
        .set(GameTransferInfo::getOrderNo, orderNo);
    if (!this.update(lambdaUpdateWrapper)) {
      throw new ServiceException("修改额度转换记录表异常");
    }
  }
}
