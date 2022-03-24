package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.RebateConfigMapper;
import com.gameplat.admin.model.vo.RebateConfigVO;
import com.gameplat.admin.service.AgentBaseService;
import com.gameplat.admin.service.RebateConfigService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.model.entity.proxy.RebateConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class RebateConfigServiceImpl extends ServiceImpl<RebateConfigMapper, RebateConfig>
    implements RebateConfigService {

  @Autowired private RebateConfigMapper rebateConfigMapper;

  @Autowired private AgentBaseService agentBaseService;

  @Override
  public List<RebateConfigVO> getRebateConfig(RebateConfig rebateConfig) {
    return rebateConfigMapper.getRebateConfig(rebateConfig);
  }

  @Override
  public IPage<RebateConfigVO> queryPage(PageDTO<RebateConfig> page, RebateConfig dto) {
    return rebateConfigMapper.queryPage(page, dto);
  }

  @Override
  public int addRebateConfig(RebateConfig rebateConfig) {
    agentBaseService.rebatePlanCheck(rebateConfig.getPlanId());
    return rebateConfigMapper.addRebateConfig(rebateConfig);
  }

  @Override
  public int editRebateConfig(RebateConfig rebateConfig) {
    int result = rebateConfigMapper.editRebateConfig(rebateConfig);
    if (result != 1) {
      throw new ServiceException("编辑平级分红方案明细失败");
    }
    return result;
  }

  @Override
  public int removeRebateConfig(String configIds, Long planId) {
    agentBaseService.planUsedCheck(planId);
    return rebateConfigMapper.removeRebateConfig(configIds);
  }

  @Override
  public RebateConfig getRebateConfigByParam(
      Long planId, BigDecimal agentProfit, Integer activityMember) {
    RebateConfig rebateConfigPO = rebateConfigMapper.getConfigByPlanId(planId);
    RebateConfig rebateConfig =
        rebateConfigMapper.getConfigByParam(planId, agentProfit, activityMember);
    if (!Objects.isNull(rebateConfig)) {
      rebateConfigPO.setConfigId(rebateConfig.getConfigId());
      rebateConfigPO.setRebateLevel(rebateConfig.getRebateLevel());
      rebateConfigPO.setAgentProfit(rebateConfig.getAgentProfit());
      rebateConfigPO.setActivityMember(rebateConfig.getActivityMember());
      rebateConfigPO.setCommission(rebateConfig.getCommission());
    }
    return rebateConfigPO;
  }
}
