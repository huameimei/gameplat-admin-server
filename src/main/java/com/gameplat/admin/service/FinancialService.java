package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.model.entity.Financial;

import java.util.Map;

public interface FinancialService extends IService<Financial> {

  int insertFinancial(Financial financial);

  // 参数校验
  void checkFinancialParam(Financial financial);

  Map getWithdrawtRanslateAmount(Long userId);
}
