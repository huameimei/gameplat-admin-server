package com.gameplat.admin.service;

import com.gameplat.model.entity.game.GamePlatform;
import com.gameplat.model.entity.game.GameTransferInfo;
import com.gameplat.model.entity.member.Member;

import java.util.List;

public interface GameBalanceService {

  String getUserGameBalanceCacheKey(Long memberId);

  void removeUserGameBalanceCache(String memberId);

  List<GameTransferInfo> getAndUpdateGameBalance(List<GamePlatform> platforms, Member member);
}
