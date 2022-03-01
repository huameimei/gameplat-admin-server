package com.gameplat.admin.controller.open.game;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.dto.MemberDayReportDto;
import com.gameplat.admin.model.vo.MemberDayReportVo;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.admin.service.GameMemberReportService;
import com.gameplat.base.common.util.DateUtil;
import com.gameplat.base.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @Author kb @Date 2022/2/8 18:26 @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/game/gameMemberReport")
public class GameMemberReportController {

  @Autowired private GameMemberReportService gameMemberReportService;

  /** 查询投注日报表记录 */
  @GetMapping(value = "/queryBetReport")
  public PageDtoVO<MemberDayReportVo> queryMemberReport(
      Page<MemberDayReportVo> page, MemberDayReportDto dto) {
    log.info("充提入参：{}", JSON.toJSONString(dto));

    if (StringUtils.isEmpty(dto.getStartTime())) {
      String beginTime = DateUtil.getDateToString(new Date());
      dto.setStartTime(beginTime);
    }

    if (StringUtils.isEmpty(dto.getEndTime())) {
      String endTime = DateUtil.getDateToString(new Date());
      dto.setEndTime(endTime);
    }
    return gameMemberReportService.findSumMemberDayReport(page, dto);
  }
}
