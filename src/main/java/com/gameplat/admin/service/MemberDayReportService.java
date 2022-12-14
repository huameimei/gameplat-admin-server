package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.AgentReportQueryDTO;
import com.gameplat.admin.model.vo.MemberDayReportVo;
import com.gameplat.admin.model.vo.MemberInfoVO;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.model.entity.member.MemberDayReport;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;

/** @Description : 会员日报表 @Author : cc @Date : 2022/3/11 */
public interface MemberDayReportService extends IService<MemberDayReport> {
  /**
   * 分页列表
   *
   * @param page
   * @param dto
   * @return
   */
  PageDtoVO<MemberDayReportVo> agentReportList(
      PageDTO<MemberDayReport> page, AgentReportQueryDTO dto);

  /**
   * 导出
   *
   * @param dto
   * @param response
   */
  void exportAgentReport(AgentReportQueryDTO dto, HttpServletResponse response);

  void addUpdateWaterAmount(MemberInfoVO member, BigDecimal realRebateMoney, String statTime);
}
