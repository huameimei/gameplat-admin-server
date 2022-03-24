package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.vo.AgentPlanVO;

public interface AgentDivideService {
  IPage<AgentPlanVO> queryPage(PageDTO<AgentPlanVO> page, Integer divideType, String agentName);

  /**
   * 代理线绑定分红方案
   *
   * @param divideType
   * @param familyIds
   * @return
   */
  int bindAgentLineDivide(Integer divideType, String familyIds);

  /**
   * 代理绑定分红方案
   *
   * @param divideType
   * @param planId
   * @param agentId
   * @return
   */
  int bindAgentDivide(Integer divideType, Long planId, Long agentId);
}
