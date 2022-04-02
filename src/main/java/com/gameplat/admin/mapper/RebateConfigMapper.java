package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.vo.RebateConfigVO;
import com.gameplat.model.entity.proxy.RebateConfig;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/** @Description : 层层代分红模式配置 @Author : cc @Date : 2022/2/22 */
public interface RebateConfigMapper extends BaseMapper<RebateConfig> {
  /**
   * 查看代理方案
   *
   * @param rebateConfig
   * @return
   */
  List<RebateConfigVO> getRebateConfig(RebateConfig rebateConfig);

  /**
   * 列表查询
   *
   * @param page
   * @param dto
   * @return
   */
  IPage<RebateConfigVO> queryPage(PageDTO<RebateConfig> page, @Param("dto") RebateConfig dto);

  /**
   * 新增返佣配置
   *
   * @param rebateConfigPO
   * @return
   */
  int addRebateConfig(RebateConfig rebateConfigPO);

  /**
   * 编辑返佣配置
   *
   * @param rebateConfigPO
   * @return
   */
  int editRebateConfig(RebateConfig rebateConfigPO);

  /**
   * 删除返佣配置
   *
   * @param configIds
   * @return
   */
  int removeRebateConfig(String configIds);

  /**
   * 根据ID获取方案配置
   *
   * @param planId
   * @return
   */
  RebateConfig getConfigByPlanId(Long planId);

  /**
   * 根据参数获取分红配置
   *
   * @param planId
   * @param agentProfit
   * @param activityMember
   * @return
   */
  RebateConfig getConfigByParam(
      @Param("planId") Long planId,
      @Param("agentProfit") BigDecimal agentProfit,
      @Param("activityMember") Integer activityMember);
}
