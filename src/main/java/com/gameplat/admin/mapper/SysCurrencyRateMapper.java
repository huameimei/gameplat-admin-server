package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.domain.SysCurrencyRate;

/**
 * 货币比例
 * */

public interface SysCurrencyRateMapper extends BaseMapper<SysCurrencyRate> {

    SysCurrencyRate getConfig();
}
