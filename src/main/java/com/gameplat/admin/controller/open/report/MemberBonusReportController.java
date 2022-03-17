package com.gameplat.admin.controller.open.report;

import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.MemberBonusReportQueryDTO;
import com.gameplat.admin.model.vo.MemberBonusReportVO;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.admin.service.MemberBonusReportService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @author aBen
 * @date 2022/3/16 20:39
 * @desc
 */
@Api(tags = "IP分析报表")
@RestController
@RequestMapping("/api/admin/report/bonus")
public class MemberBonusReportController {

  @Autowired
  private MemberBonusReportService memberBonusReportService;

  @GetMapping(value = "/findReportPage")
  @ApiOperation(value = "查询会员红利报表")
  public PageDtoVO<MemberBonusReportVO> findMemberBonusReportPage(PageDTO<MemberBonusReportVO> page, MemberBonusReportQueryDTO queryDTO) {
    return memberBonusReportService.findMemberBonusReportPage(page, queryDTO);
  }

  @GetMapping(value = "/exportReport")
  @ApiOperation(value = "导出会员红利报表")
  @Log(module = ServiceName.ADMIN_SERVICE, desc = "导出会员红利报表")
  public void findMemberBonusReportPage(MemberBonusReportQueryDTO queryDTO, HttpServletResponse response) {
    memberBonusReportService.exportMemberBonusReport(queryDTO, response);
  }

}
