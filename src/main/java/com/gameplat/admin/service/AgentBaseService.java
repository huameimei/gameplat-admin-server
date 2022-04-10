package com.gameplat.admin.service;

import java.math.BigDecimal;

public interface AgentBaseService {
  void rebatePlanCheck(Long planId);

  void planUsedCheck(Long planId);

  void settle(
      String countDate, String operateBy, Long agentId, String agentName, BigDecimal settleMoney);
}
