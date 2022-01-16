package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.GameTransferInfo;

public interface GameTransferInfoService extends IService<GameTransferInfo> {

  GameTransferInfo getInfoByMemberId(Long memberId);

  void update(Long memberId, String code, String orderNo);
}
