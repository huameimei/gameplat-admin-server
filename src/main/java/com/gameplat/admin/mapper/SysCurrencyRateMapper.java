package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.model.entity.sys.SysCurrencyRate;

/** 货币比例 */
public interface SysCurrencyRateMapper extends BaseMapper<SysCurrencyRate> {

  SysCurrencyRate getConfig();
}
