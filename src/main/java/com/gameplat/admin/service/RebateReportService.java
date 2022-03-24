package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.RebateReportDTO;
import com.gameplat.admin.model.vo.*;
import com.gameplat.model.entity.proxy.RebateReport;

import java.util.List;

public interface RebateReportService extends IService<RebateReport> {
  IPage<RebateReportVO> queryPage(PageDTO<AgentPlanVO> page, RebateReportDTO rebateReportDTO);

  List<RebateReportVO> getRebateReport(RebateReportDTO rebateReportDTO);

  void updateRebateReport(String countDate, String agentName);

  List<MemberReportVO> getMemberReport(Long agentId, String countMonth);

  IPage<MemberReportVO> pageMemberReport(
      PageDTO<MemberReportVO> page, Long agentId, String countDate);

  Page<PlatformFeeVO> gameWin(PageDTO<PlatformFeeVO> page, Long agentId, String countDate);

  /**
   * 获取平台费用
   *
   * @param agentId
   * @param countMonth
   * @return
   */
  List<PlatformFeeVO> getPlatformFee(Long agentId, String countMonth);

  /**
   * 获取平台费用统计数据
   *
   * @param agentId
   * @param countMonth
   * @return
   */
  GameWinVO getPlatformFeeSum(Long agentId, String countMonth);

  List<CompanyCostVO> pagePlatformCost(Long agentId, String countDate);

  /**
   * 查看会员佣金报表
   *
   * @param agentId
   * @param countMonth
   * @return
   */
  MemberCommissionVO getMemberCommission(Long agentId, String countMonth);

  /**
   * 查看代理佣金报表
   *
   * @param agentId
   * @param countMonth
   * @return
   */
  AgentCommissionVO getAgentCommission(Long agentId, String countMonth);

  /**
   * 查看下级代理佣金报表
   *
   * @param agentId
   * @param countMonth
   * @return
   */
  Page<AgentCommissionVO> getSubAgentCommission(
      PageDTO<AgentCommissionVO> page, Long agentId, String countMonth);

  /**
   * 审核或结算
   *
   * @param currentStatus
   * @param reportId
   */
  int reviewOrSettlement(Integer currentStatus, Long reportId);

  /**
   * 批量审核或结算
   *
   * @param currentStatus
   * @param countDate
   */
  int batchReviewOrSettlement(Integer currentStatus, String countDate);

  /**
   * 更新实际佣金
   *
   * @param reportId
   * @return
   */
  int updateActualCommission(Long reportId);
}
