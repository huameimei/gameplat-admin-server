package com.gameplat.admin.controller.open.report;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.MemberBonusReportQueryDTO;
import com.gameplat.admin.model.vo.MemberBonusReportVO;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.admin.service.MemberBonusReportService;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Date;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author aBen
 * @date 2022/3/16 20:39
 * @desc 会员红利报表
 */
@Api(tags = "会员红利报表")
@RestController
@RequestMapping("/api/admin/report/bonus")
public class MemberBonusReportController {

  @Autowired private MemberBonusReportService memberBonusReportService;

  @GetMapping(value = "/findReportPage")
  @PreAuthorize("hasAuthority('report:dividendtDataReport:view')")
  @ApiOperation(value = "查询会员红利报表")
  public PageDtoVO<MemberBonusReportVO> findReportPage(
      PageDTO<MemberBonusReportVO> page, MemberBonusReportQueryDTO queryDTO) {
    if (StringUtils.isEmpty(queryDTO.getStartTime())
        || StringUtils.isEmpty(queryDTO.getEndTime())) {
      queryDTO.setStartTime(DateUtil.format(new Date(), "YYYY-MM-dd"));
      queryDTO.setEndTime(DateUtil.format(new Date(), "YYYY-MM-dd"));
    }
    return memberBonusReportService.findMemberBonusReportPage(page, queryDTO);
  }

  @GetMapping(value = "/exportReport")
  @ApiOperation(value = "导出会员红利报表")
  @PreAuthorize("hasAuthority('bonus:report:export')")
  @Log(module = ServiceName.ADMIN_SERVICE, desc = "导出会员红利报表")
  public void exportReport(MemberBonusReportQueryDTO queryDTO, HttpServletResponse response) {
    if (StringUtils.isEmpty(queryDTO.getStartTime())
        || StringUtils.isEmpty(queryDTO.getEndTime())) {
      queryDTO.setStartTime(DateUtil.format(new Date(), "YYYY-MM-dd"));
      queryDTO.setEndTime(DateUtil.format(new Date(), "YYYY-MM-dd"));
    }
    memberBonusReportService.exportMemberBonusReport(queryDTO, response);
  }
}
