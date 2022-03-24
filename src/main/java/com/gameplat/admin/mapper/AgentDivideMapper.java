package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.vo.AgentPlanVO;
import com.gameplat.model.entity.proxy.RebateReport;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface AgentDivideMapper {
  /**
   * 检查方案是否在被使用
   *
   * @param divideType
   * @param planId
   * @return
   */
  int checkPlanUsed(@Param("divideType") Integer divideType, @Param("planId") Long planId);

  /**
   * 检查方案是否被使用
   *
   * @param divideType
   * @param planId
   * @return
   */
  int agentPlanCheck(@Param("divideType") Integer divideType, @Param("planId") Long planId);

  IPage<AgentPlanVO> queryPage(PageDTO<AgentPlanVO> page, Integer divideType, String agentName);

  /**
   * 代理线绑定分红方案
   *
   * @param divideType
   * @param planId
   * @param memberIds
   * @return
   */
  int bindAgentLineDivide(
      @Param("divideType") Integer divideType,
      @Param("planId") Long planId,
      @Param("memberIds") Set<Long> memberIds);

  Set<String> getSuperProxyName(@Param("superProxyIds") Set<Long> superProxyIds);

  Set<Long> getLowerIds(@Param("superProxyNames") Set<String> superProxyNames);

  /**
   * 代理绑定分红方案
   *
   * @param divideType
   * @param planId
   * @param agentId
   * @return
   */
  int bindAgentDivide(
      @Param("divideType") Integer divideType,
      @Param("planId") Long planId,
      @Param("agentId") Long agentId);

  /**
   * 代理佣金报表
   *
   * @param agentName
   * @return
   */
  List<RebateReport> agentRebateReport(String agentName);
}
