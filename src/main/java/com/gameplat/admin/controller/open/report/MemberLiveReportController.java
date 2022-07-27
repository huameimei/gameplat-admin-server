package com.gameplat.admin.controller.open.report;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.MemberLiveReportDto;
import com.gameplat.admin.model.vo.MemberLiveReportVo;
import com.gameplat.admin.service.MemberLiveReportService;
import com.gameplat.base.common.util.DateUtil;
import com.gameplat.base.common.util.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Tag(name = "会员活跃度")
@RestController
@RequestMapping("/api/admin/report/LiveReport")
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
}
