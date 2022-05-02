package com.gameplat.admin.controller.open.member;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.mapper.MemberBusDayReportMapper;
import com.gameplat.admin.model.dto.DayReportDTO;
import com.gameplat.admin.model.dto.MemberReportDto;
import com.gameplat.admin.model.dto.MemberbetAnalysisdto;
import com.gameplat.admin.model.vo.DayReportVO;
import com.gameplat.admin.model.vo.MemberGameDayReportVo;
import com.gameplat.admin.model.vo.MemberbetAnalysisVo;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.admin.service.GameMemberReportService;
import com.gameplat.base.common.util.DateUtil;
import com.gameplat.base.common.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * 会员日报表
 *
 * @author lily
 */
@Api(tags = "会员日报表")
@Slf4j
@RestController
@RequestMapping("/api/admin/member/dayReport")
public class MemberDayReportController {

  @Autowired(required = false)
  private MemberBusDayReportMapper memberBusDayReportMapper;

  @Autowired private GameMemberReportService gameMemberReportService;

  @GetMapping("/list")
  @ApiOperation("查询会员日报列表")
  @PreAuthorize("hasAuthority('member:dayReport:list')")
  public List<DayReportVO> findList(DayReportDTO dto) {
    return memberBusDayReportMapper.findList(dto);
  }

  @ApiOperation("会员日报表")
  @GetMapping("memberGameDayReport")
  @PreAuthorize("hasAuthority('member:dayReport:view')")
  public PageDtoVO<MemberGameDayReportVo> queryBetReport(
      Page<MemberGameDayReportVo> page, MemberReportDto dto) {
    log.info("会员日报表入参：{}", JSON.toJSONString(dto));
    if (StringUtils.isEmpty(dto.getStartTime())) {
      String beginTime = DateUtil.getDateToString(new Date());
      dto.setStartTime(beginTime);
    }
    if (StringUtils.isEmpty(dto.getEndTime())) {
      String endTime = DateUtil.getDateToString(new Date());
      dto.setEndTime(endTime);
    }
    return gameMemberReportService.findSumMemberGameDayReport(page, dto);
  }

  @ApiOperation("投注分析")
  @GetMapping("findMemberbetAnalysis")
  @PreAuthorize("hasAuthority('member:dayReport:betAnalysis')")
  public MemberbetAnalysisVo findMemberbetAnalysis(MemberbetAnalysisdto dto) {
    log.info("会员日报表入参：{}", JSON.toJSONString(dto));
    if (StringUtils.isEmpty(dto.getStartTime())) {
      String beginTime = DateUtil.getDateToString(new Date());
      dto.setStartTime(beginTime);
    }
    if (StringUtils.isEmpty(dto.getEndTime())) {
      String endTime = DateUtil.getDateToString(new Date());
      dto.setEndTime(endTime);
    }
    return gameMemberReportService.findMemberbetAnalysis(dto);
  }
}
