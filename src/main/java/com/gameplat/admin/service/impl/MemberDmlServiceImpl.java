package com.gameplat.admin.service.impl;

import com.gameplat.admin.service.MemberDmlService;
import com.gameplat.common.enums.GameKindEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class MemberDmlServiceImpl implements MemberDmlService {

  @Override
  public void calcGameDml(GameKindEnum gameCode) {
    // TODO: 17/06/2021 计算用户打码量

  }
}
