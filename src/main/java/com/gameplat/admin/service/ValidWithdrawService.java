package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.RechargeOrder;
import com.gameplat.admin.model.domain.ValidWithdraw;
import com.gameplat.admin.model.vo.ValidateDmlBeanVo;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.common.model.bean.limit.MemberWithdrawLimit;

import java.util.Date;


public interface ValidWithdrawService extends IService<ValidWithdraw> {

  void addRechargeOrder(RechargeOrder rechargeOrder) throws Exception;

  void remove(Long memberId, Date cashDate) throws Exception;

  int saveValidWithdraw(ValidWithdraw validWithdraw);


  ValidateDmlBeanVo validateByMemberId(MemberWithdrawLimit memberWithdrawLimit, String username, boolean ignoreOuted)
          throws ServiceException;

  void countAccountValidWithdraw(String username);
}
