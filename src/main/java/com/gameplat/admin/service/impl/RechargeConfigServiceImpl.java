package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.constant.RechargeMode;
import com.gameplat.admin.mapper.RechargeConfigMapper;
import com.gameplat.admin.model.domain.RechargeConfig;
import com.gameplat.admin.service.RechargeConfigService;
import com.gameplat.common.exception.ServiceException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class RechargeConfigServiceImpl extends ServiceImpl<RechargeConfigMapper, RechargeConfig>
    implements RechargeConfigService {

  @Autowired
  private RechargeConfigMapper rechargeConfigMapper;


  @Override
  public void add(RechargeConfig rechargeConfig) throws ServiceException {
    if (rechargeConfig.getMode() == RechargeMode.TRANSFER.getValue()
        && rechargeConfig.getDisableAfterCancelled() == null) {
      throw new ServiceException("请配置取消入款后是否限制入款");
    }
    RechargeConfig uniqueRechargeConfig = this.lambdaQuery()
        .eq(RechargeConfig::getMode, rechargeConfig.getMode())
        .eq(RechargeConfig::getPayType, rechargeConfig.getPayType()).one();
    if (rechargeConfig.getId() == null && uniqueRechargeConfig != null) {
      throw new ServiceException("该支付类型的充值配置已存在,请刷新页面");
    }
    this.saveOrUpdate(rechargeConfig);
  }

  @Override
  public List<RechargeConfig> queryAll() {
    return this.list();
  }

}