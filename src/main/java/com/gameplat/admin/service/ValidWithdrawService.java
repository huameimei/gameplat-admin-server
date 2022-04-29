package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.ValidWithdrawDto;
import com.gameplat.admin.model.vo.ValidateDmlBeanVo;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.common.model.bean.limit.MemberWithdrawLimit;
import com.gameplat.model.entity.ValidWithdraw;
import com.gameplat.model.entity.recharge.RechargeOrder;

import java.util.Date;

public interface ValidWithdrawService extends IService<ValidWithdraw> {

  void addRechargeOrder(RechargeOrder rechargeOrder);

  void remove(Long memberId, Date cashDate) throws Exception;

  int saveValidWithdraw(ValidWithdraw validWithdraw);

  ValidateDmlBeanVo validateByMemberId(
      MemberWithdrawLimit memberWithdrawLimit, String username, boolean ignoreOuted)
      throws ServiceException;

  void countAccountValidWithdraw(String username);

  void updateValidWithdraw(ValidWithdrawDto dto);

  void delValidWithdraw(String member) throws Exception;

  /**
   * 回收游戏返水，清除对应打码量
   *
   * @param remark String
   */
  void rollGameRebateDml(String remark);
}
