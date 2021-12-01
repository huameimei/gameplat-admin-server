package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.RechargeOrder;
import com.gameplat.admin.model.domain.ValidWithdraw;
import java.util.Date;


public interface ValidWithdrawService extends IService<ValidWithdraw> {

  void addRechargeOrder(RechargeOrder rechargeOrder) throws Exception;

  void remove(Long memberId, Date cashDate) throws Exception;

  int saveValidWithdraw(ValidWithdraw validWithdraw);
}
