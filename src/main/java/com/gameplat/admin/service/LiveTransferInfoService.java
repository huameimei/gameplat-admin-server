package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.LiveTransferInfo;

public interface LiveTransferInfoService extends IService<LiveTransferInfo> {

  LiveTransferInfo getInfoByMemberId(Long memberId);

  void update(Long memberId, String code, String orderNo);
}
