package com.gameplat.admin.controller.open.report;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.dto.DepositReportDto;
import com.gameplat.admin.model.vo.MemberRWReportVo;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.admin.service.GameMemberReportService;
import com.gameplat.base.common.util.DateUtil;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.annotation.PageAsQueryParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Tag(name = "IP分析报表")
@RestController
@RequestMapping("/api/admin/report/memberReport")
public class MemberReportController {

  @Autowired private GameMemberReportService gameMemberReportService;

  @Operation(summary = "分页查询")
  @PageAsQueryParam
  @GetMapping(value = "pageQueryDepositReport")
  @PreAuthorize("hasAuthority('report:memberReport:view')")
  public PageDtoVO<MemberRWReportVo> pageQueryDepositReport(
      @Parameter(hidden = true) Page<MemberRWReportVo> page, DepositReportDto dto) {
    if (StringUtils.isEmpty(dto.getStartTime())) {
      String beginTime = DateUtil.getDateToString(new Date());
      dto.setStartTime(beginTime);
    }
    if (StringUtils.isEmpty(dto.getEndTime())) {
      String endTime = DateUtil.getDateToString(new Date());
      dto.setEndTime(endTime);
    }
    return gameMemberReportService.findSumMemberRWReport(page, dto);
  }
}
