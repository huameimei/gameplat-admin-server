package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.vo.RebateConfigVO;
import com.gameplat.model.entity.proxy.RebateConfig;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/** @Description : 平级分红方案 @Author : cc @Date : 2022/4/2 */
public interface RebateConfigService extends IService<RebateConfig> {

  /**
   * 查看返佣配置
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
  IPage<RebateConfigVO> queryPage(PageDTO<RebateConfig> page, RebateConfig dto);

  /**
   * 新增返佣配置
   *
   * @param rebateConfig
   * @return
   */
  int addRebateConfig(RebateConfig rebateConfig);

  /**
   * 编辑返佣配置
   *
   * @param rebateConfig
   * @return
   */
  int editRebateConfig(RebateConfig rebateConfig);

  /**
   * 删除返佣配置
   *
   * @param configIds
   * @param planId
   * @return
   */
  int removeRebateConfig(String configIds, Long planId);

  /**
   * 根据参数获取分红配置
   *
   * @param planId
   * @param agentProfit
   * @param activityMember
   * @return
   */
  RebateConfig getRebateConfigByParam(
      @Param("planId") Long planId,
      @Param("agentProfit") BigDecimal agentProfit,
      @Param("activityMember") Integer activityMember);
}
