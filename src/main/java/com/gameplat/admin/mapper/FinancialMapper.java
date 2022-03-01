package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.model.entity.Financial;

import java.util.Map;

/**
 * @author lily
 * @since 2021-11-27
 */
public interface FinancialMapper extends BaseMapper<Financial> {

  int saveFinancial(Financial financial);

  Map getWithdrawtRanslateAmount(Long userId);
}
