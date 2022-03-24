package com.gameplat.admin.service.impl;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.mapper.AgentDivideMapper;
import com.gameplat.admin.mapper.RebatePlanMapper;
import com.gameplat.admin.model.vo.AgentPlanVO;
import com.gameplat.admin.model.vo.RebatePlanVO;
import com.gameplat.admin.service.AgentBaseService;
import com.gameplat.admin.service.AgentDivideService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.model.entity.proxy.RebatePlan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class AgentDivideServiceImpl implements AgentDivideService {
  @Autowired private AgentDivideMapper agentDivideMapper;
  @Autowired private RebatePlanMapper rebatePlanMapper;
  @Autowired private AgentBaseService agentBaseService;

  @Override
  public IPage<AgentPlanVO> queryPage(
      PageDTO<AgentPlanVO> page, Integer divideType, String agentName) {
    return agentDivideMapper.queryPage(page, divideType, agentName);
  }

  /**
   * 代理线绑定分红方案
   *
   * @param divideType
   * @param familyIds
   * @return
   */
  @Override
  public int bindAgentLineDivide(Integer divideType, String familyIds) {
    Long planId = 0L;
    // 平级分红默认使用官网方案
    if (divideType == 1) {
      List<RebatePlanVO> planList =
          rebatePlanMapper.getRebatePlan(
              new RebatePlan() {
                {
                  setDefaultFlag(1);
                }
              });
      planId = planList.get(0).getPlanId();
    }
    // 根据familyIds 查找出所有 对应代理线的所有id
    Set<Long> set = Convert.toSet(Long.class, familyIds);
    Set<String> superProxyNames = agentDivideMapper.getSuperProxyName(set);
    Set<Long> lowerIds = agentDivideMapper.getLowerIds(superProxyNames);
    int result = agentDivideMapper.bindAgentLineDivide(divideType, planId, lowerIds);
    if (result == 0) {
      throw new ServiceException("代理线绑定佣金方案失败");
    }
    return result;
  }

  /**
   * 代理绑定分红方案
   *
   * @param divideType
   * @param planId
   * @param agentId
   * @return
   */
  @Override
  public int bindAgentDivide(Integer divideType, Long planId, Long agentId) {
    agentBaseService.rebatePlanCheck(planId);
    int result = agentDivideMapper.bindAgentDivide(divideType, planId, agentId);
    if (result != 1) {
      throw new ServiceException("代理绑定佣金方案失败");
    }
    return result;
  }
}
