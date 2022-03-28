package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.RebateExtendMapper;
import com.gameplat.admin.mapper.RebateReportMapper;
import com.gameplat.admin.model.vo.RebateReportVO;
import com.gameplat.admin.service.RebateExtendService;
import com.gameplat.admin.service.RebateReportService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.model.entity.proxy.RebateReportExtend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class RebateExtendServiceImpl extends ServiceImpl<RebateExtendMapper, RebateReportExtend>
    implements RebateExtendService {

  @Autowired private RebateExtendMapper rebateExtendMapper;

  @Autowired private RebateReportMapper reportMapper;

  @Autowired private RebateReportService rebateReportService;

  @Override
  public IPage<RebateReportExtend> queryPage(PageDTO<RebateReportExtend> page, Long reportId) {
    return rebateExtendMapper.queryPage(page, reportId);
  }

  @Override
  public int addReportExtend(RebateReportExtend reportExtendPO) {
    // 佣金报表已结算
    reportStatus(reportExtendPO.getReportId(), null);
    // 新增佣金调整记录
    rebateExtendMapper.addReportExtend(reportExtendPO);
    // 更新调整金额
    reportMapper.updateAdjustmentAmount(reportExtendPO.getReportId());
    // 重新计算实际佣金
    rebateReportService.updateActualCommission(reportExtendPO.getReportId());
    return 1;
  }

  @Override
  public int editReportExtend(RebateReportExtend reportExtendPO) {
    // 佣金报表已结算
    reportStatus(null, reportExtendPO.getExtendId());
    // 新增佣金调整记录
    rebateExtendMapper.editReportExtend(reportExtendPO);
    // 更新调整金额
    reportMapper.updateAdjustmentAmount(reportExtendPO.getReportId());
    // 重新计算实际佣金
    rebateReportService.updateActualCommission(reportExtendPO.getReportId());
    return 1;
  }

  @Override
  public int removeReportExtend(RebateReportExtend reportExtendPO) {
    // 佣金报表已结算
    reportStatus(null, reportExtendPO.getExtendId());
    // 新增佣金调整记录
    rebateExtendMapper.removeReportExtend(reportExtendPO);
    // 更新调整金额
    reportMapper.updateAdjustmentAmount(reportExtendPO.getReportId());
    // 重新计算实际佣金
    rebateReportService.updateActualCommission(reportExtendPO.getReportId());
    return 1;
  }

  public void reportStatus(Long reportId, Long extendId) {
    RebateReportVO reportVO = reportMapper.getReportByReportId(reportId, extendId);
    if (Objects.isNull(reportVO)) {
      throw new ServiceException("参数错误");
    } else if (reportVO.getStatus() == 3) {
      throw new ServiceException("佣金报表已结算");
    } else if (reportVO.getAccountStatus() == 0) {
      throw new ServiceException("该代理账号已停用");
    }
  }
}
