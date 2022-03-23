package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.AgentDivideMapper;
import com.gameplat.admin.mapper.RebatePlanMapper;
import com.gameplat.admin.model.vo.RebatePlanVO;
import com.gameplat.admin.service.RebatePlanService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.model.entity.proxy.RebatePlan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class RebatePlanServiceImpl extends ServiceImpl<RebatePlanMapper, RebatePlan>
    implements RebatePlanService {

  @Autowired private RebatePlanMapper rebatePlanMapper;

  @Autowired private AgentDivideMapper agentDivideMapper;

  @Override
  public IPage<RebatePlanVO> queryPage(PageDTO<RebatePlan> page, RebatePlan dto) {
    return rebatePlanMapper.queryPage(page, dto);
  }

  @Override
  public int addRebatePlan(RebatePlan rebatePlanPO) {
    return rebatePlanMapper.addRebatePlan(rebatePlanPO);
  }

  @Override
  public int editRebatePlan(RebatePlan rebatePlanPO) {
    int result = rebatePlanMapper.editRebatePlan(rebatePlanPO);
    if (result != 1) {
      throw new ServiceException("编辑平级分红方案失败");
    }
    return result;
  }

  @Override
  public int removeRebatePlan(Long planId) {
    List<RebatePlanVO> rebatePlanPOList =
        rebatePlanMapper.getRebatePlan(
            new RebatePlan() {
              {
                setPlanId(planId);
              }
            });
    if (rebatePlanPOList.get(0).getDefaultFlag() == 1) {
      throw new ServiceException("官网方案不能删除");
    }
    if (agentDivideMapper.checkPlanUsed(1, planId) > 0) {
      throw new ServiceException("该方案正在被使用");
    }
    return rebatePlanMapper.removeRebatePlan(planId);
  }
}
