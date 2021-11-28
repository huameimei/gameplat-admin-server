package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.SysCurrencyRate;

/**
 * 货币比例
 * */
public interface SysCurrencyRateService extends IService<SysCurrencyRate> {

    /** 查询货币兑换比例 */
    SysCurrencyRate info();
}
