package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.RebateReportDTO;
import com.gameplat.admin.model.vo.*;
import com.gameplat.model.entity.proxy.RebateReport;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RebateReportMapper extends BaseMapper<RebateReport> {
  IPage<RebateReportVO> queryPage(
      PageDTO<AgentPlanVO> page, @Param("dto") RebateReportDTO rebateReportDTO);

  List<RebateReportVO> getRebateReport(RebateReportDTO rebateReportDTO);

  /**
   * 获取已结算的代理账号
   *
   * @param countMonth
   * @return
   */
  List<String> getSettlementAgent(String countMonth);

  /**
   * 获取下级会员报表
   *
   * @param agentId
   * @param countMonth
   * @return
   */
  List<MemberReportVO> getSubMemberReport(
      @Param("agentId") Long agentId, @Param("countMonth") String countMonth);

  /**
   * 获取下级会员报表
   *
   * @param agentId
   * @param countMonth
   * @return
   */
  IPage<MemberReportVO> pageSubMemberReport(
      PageDTO<MemberReportVO> page,
      @Param("agentId") Long agentId,
      @Param("countMonth") String countMonth);

  /**
   * 获取平台费
   *
   * @param agentId
   * @param countMonth
   * @return
   */
  List<PlatformFeeVO> getPlatformFee(
      @Param("agentId") Long agentId, @Param("countMonth") String countMonth);

  /**
   * 获取平台费
   *
   * @param agentId
   * @param countMonth
   * @return
   */
  Page<PlatformFeeVO> pagePlatformFee(
      PageDTO<PlatformFeeVO> page,
      @Param("agentId") Long agentId,
      @Param("countMonth") String countMonth);

  /**
   * 获取下级会员佣金详情
   *
   * @param agentId
   * @param countDate
   * @return
   */
  MemberCommissionVO getMemberCommission(
      @Param("agentId") Long agentId, @Param("countDate") String countDate);

  /**
   * 批量新增佣金报表
   *
   * @param reportPOList
   * @return
   */
  int batchAddRebateReport(@Param("list") List<RebateReport> reportPOList);

  /**
   * 获取平台费用统计数据
   *
   * @param agentId
   * @param countMonth
   * @return
   */
  GameWinVO getPlatformFeeSum(
      @Param("agentId") Long agentId, @Param("countMonth") String countMonth);

  /**
   * 获取代理佣金数据
   *
   * @param agentId
   * @param countDate
   * @return
   */
  AgentCommissionVO getAgentCommission(
      @Param("agentId") Long agentId, @Param("countDate") String countDate);

  Page<AgentCommissionVO> pageSubAgentCommission(
      PageDTO<AgentCommissionVO> page,
      @Param("agentId") Long agentId,
      @Param("countMonth") String countMonth);

  /**
   * 查看结算状态
   *
   * @param reportId
   * @param extendId
   * @return
   */
  RebateReportVO getReportByReportId(
      @Param("reportId") Long reportId, @Param("extendId") Long extendId);

  /**
   * 更新结算状态
   *
   * @param reportId
   * @param updateBy
   * @return
   */
  int updateStatus(
      @Param("reportId") Long reportId,
      @Param("status") Integer status,
      @Param("updateBy") String updateBy);

  /**
   * 批量更新状态
   *
   * @param countDate
   * @param countDate
   * @param countDate
   * @return
   */
  int batchUpdateStatus(
      @Param("countDate") String countDate,
      @Param("updateBy") String updateBy,
      @Param("status") Integer status);
}
