package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.MemberInfo;

import java.math.BigDecimal;

public interface MemberInfoService extends IService<MemberInfo> {

  void updateMemberWithdraw(MemberInfo memberInfo, BigDecimal cashMoney);

  void updateMemberRech(MemberInfo memberInfo, BigDecimal momey);

}
