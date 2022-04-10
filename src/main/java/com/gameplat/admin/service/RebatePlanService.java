package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.vo.RebatePlanVO;
import com.gameplat.model.entity.proxy.RebatePlan;

/** @Description : 平级分红方案 @Author : cc @Date : 2022/4/2 */
public interface RebatePlanService extends IService<RebatePlan> {
  /**
   * 分页列表
   *
   * @param page
   * @param dto
   * @return
   */
  IPage<RebatePlanVO> queryPage(PageDTO<RebatePlan> page, RebatePlan dto);

  /**
   * 新增代理方案
   *
   * @param rebatePlanPO
   * @return
   */
  int addRebatePlan(RebatePlan rebatePlanPO);

  /**
   * 编辑代理方案
   *
   * @param rebatePlanPO
   * @return
   */
  int editRebatePlan(RebatePlan rebatePlanPO);

  /**
   * 删除代理方案
   *
   * @param planId
   * @return
   */
  int removeRebatePlan(Long planId);
}
