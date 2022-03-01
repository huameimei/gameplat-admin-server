package com.gameplat.admin.controller.open.report;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.dto.DepositReportDto;
import com.gameplat.admin.model.vo.MemberRWReportVo;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.admin.service.GameMemberReportService;
import com.gameplat.base.common.util.DateUtil;
import com.gameplat.base.common.util.StringUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @Author kb @Date 2022/2/13 18:33 @Version 1.0
 */
@Slf4j
@Api(tags = "IP分析报表")
@RestController
@RequestMapping("/api/admin/report/memberReport")
public class MemberReportController {

  @Autowired private GameMemberReportService gameMemberReportService;

  /**
   * 充提报表
   *
   * @param page
   * @param dto
   * @return
   */
  @GetMapping(value = "pageQueryDepositReport")
  public PageDtoVO<MemberRWReportVo> pageQueryDepositReport(
      Page<MemberRWReportVo> page, DepositReportDto dto) {
    log.info("充提入参：{}", JSON.toJSONString(dto));
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
