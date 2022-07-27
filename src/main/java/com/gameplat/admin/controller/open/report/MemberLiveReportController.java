package com.gameplat.admin.controller.open.report;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.MemberLiveReportDto;
import com.gameplat.admin.model.vo.MemberLiveReportVo;
import com.gameplat.admin.service.MemberLiveReportService;
import com.gameplat.base.common.util.DateUtil;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.security.SecurityUserHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.rmi.ServerException;
import java.util.Date;

@Tag(name = "会员活跃度")
@RestController
@RequestMapping("/api/admin/report/liveReport")
@Slf4j
public class MemberLiveReportController {

  @Autowired private MemberLiveReportService memberLiveReportService;

  @Operation(summary = "会员活跃度报表")
  @GetMapping("/list")
  @PreAuthorize("hasAuthority('member:liveReport:list')")
  public Page<MemberLiveReportVo> list(PageDTO<MemberLiveReportDto> page, MemberLiveReportDto dto) {
    log.info("会员活跃度查询：{}", JSONUtil.toJsonStr(dto));
    if (StringUtils.isEmpty(dto.getStartDate())) {
      String beginTime = DateUtil.getDateToString(new Date());
      dto.setStartDate(beginTime);
    }
    if (StringUtils.isEmpty(dto.getEndDate())) {
      String endTime = DateUtil.getDateToString(new Date());
      dto.setEndDate(endTime);
    }
    return memberLiveReportService.queryPage(page, dto);
  }

  @Operation(summary = "导出会员活跃度报表")
  @PostMapping("/export")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.AGENT, desc = "'导出会员活跃度报表'")
  public void memberListExport(@RequestBody MemberLiveReportDto dto, HttpServletResponse response)
      throws IOException {
    log.info("会员活跃度导出的操作人：{}", SecurityUserHolder.getUsername());
    Integer count = memberLiveReportService.exportCount(dto);
    if (count > 500000) {
      throw new ServerException("单次导出最大数量不能超过500000条!");
    }
    memberLiveReportService.export(dto, count, response);
  }
}
