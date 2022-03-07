package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.model.entity.member.Member;
import com.gameplat.model.entity.member.MemberRwReport;
import com.gameplat.model.entity.member.MemberWithdraw;
import com.gameplat.model.entity.recharge.RechargeOrder;

public interface MemberRwReportService extends IService<MemberRwReport> {

  void addRecharge(Member member, Integer rechargeCount, RechargeOrder rechargeOrder) throws Exception;

  void addWithdraw(Member member, Integer withdrawCount, MemberWithdraw memberWithdraw)
      throws Exception;

}
