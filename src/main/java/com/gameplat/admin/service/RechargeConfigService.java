package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.model.entity.recharge.RechargeConfig;

import java.util.List;

public interface RechargeConfigService extends IService<RechargeConfig> {

  /** 保存 */
  void add(RechargeConfig rechargeConfig);

  /**
   * 充值限制列表
   *
   * @return List
   */
  List<RechargeConfig> queryAll(Integer memberLevel, Integer mode, String payCode);
}
