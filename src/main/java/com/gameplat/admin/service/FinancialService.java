package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.Financial;
import java.util.Map;

public interface FinancialService extends IService<Financial> {

    int insert(Financial financial);

    // 参数校验
    void checkFinancialParam(Financial financial);

    Map getWithdrawtRanslateAmount(Long userId);

}
