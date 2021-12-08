package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.LiveTransferInfoMapper;
import com.gameplat.admin.model.domain.LiveTransferInfo;
import com.gameplat.admin.service.LiveTransferInfoService;
import com.gameplat.base.common.exception.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class LiveTransferInfoServiceImpl extends
    ServiceImpl<LiveTransferInfoMapper, LiveTransferInfo> implements LiveTransferInfoService {

  @Override
  public LiveTransferInfo getInfoByMemberId(Long memberId) {
    return this.lambdaQuery()
        .eq(ObjectUtils.isNotNull(memberId),LiveTransferInfo::getMemberId,memberId).one();
  }

  @Override
  public void update(Long memberId, String code, String orderNo) {
    LambdaUpdateWrapper<LiveTransferInfo> lambdaUpdateWrapper = new LambdaUpdateWrapper();
    lambdaUpdateWrapper.eq(LiveTransferInfo::getMemberId,memberId)
        .set(LiveTransferInfo::getLiveCode,code)
        .set(LiveTransferInfo::getOrderNo,orderNo);
    if (!this.update(lambdaUpdateWrapper)){
      throw new ServiceException("修改额度转换记录表异常");
    }
  }
}
