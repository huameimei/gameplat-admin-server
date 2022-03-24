package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.vo.RebatePlanVO;
import com.gameplat.model.entity.proxy.RebatePlan;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/** @Description : 层层代分红模式配置 @Author : cc @Date : 2022/2/22 */
public interface RebatePlanMapper extends BaseMapper<RebatePlan> {

  /**
   * 查看代理方案
   *
   * @param rebatePlanPO
   * @return
   */
  List<RebatePlanVO> getRebatePlan(RebatePlan rebatePlanPO);

  IPage<RebatePlanVO> queryPage(PageDTO<RebatePlan> page, @Param("dto") RebatePlan dto);

  int addRebatePlan(RebatePlan rebatePlanPO);

  int editRebatePlan(RebatePlan rebatePlanPO);

  /**
   * 删除代理方案
   *
   * @param planId
   * @return
   */
  int removeRebatePlan(Long planId);
}
