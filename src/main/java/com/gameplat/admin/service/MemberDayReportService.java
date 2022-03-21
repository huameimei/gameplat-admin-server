package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.AgentReportQueryDTO;
import com.gameplat.admin.model.vo.MemberDayReportVo;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.model.entity.member.MemberDayReport;

import javax.servlet.http.HttpServletResponse;

/**
 * @Description : 会员日报表 @Author : cc @Date : 2022/3/11
 */
public interface MemberDayReportService extends IService<MemberDayReport> {
  PageDtoVO<MemberDayReportVo> agentReportList(
      PageDTO<MemberDayReport> page, AgentReportQueryDTO dto);

  void exportAgentReport(AgentReportQueryDTO dto, HttpServletResponse response);
}
