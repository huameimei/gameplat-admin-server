package com.gameplat.admin.controller.open.report;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.dto.GameRWDataReportDto;
import com.gameplat.admin.model.vo.*;
import com.gameplat.admin.service.DataReportService;
import com.gameplat.admin.service.MemberService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.DateUtils;
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
 * @Author kb @Date 2022/3/2 21:48 @Version 1.0
 */
@Slf4j
@Api(tags = "数据统计")
@RestController
@RequestMapping("/api/admin/report/data")
public class DataReportController {

  // 代理下的会员
  private final int ONE = 0;

  @Autowired(required = false)
  private DataReportService dataReportService;
  @Autowired private MemberService memberService;

  @ApiOperation("充值数据统计")
  @GetMapping("findRechReport")
  @PreAuthorize("hasAuthority('thirdParty:rechReport:count')")
  public GameRechDataReportVO findRechReport(GameRWDataReportDto dto) {
    log.info("查询数据统计充值入参：{}", JSON.toJSONString(dto));
    editGameRwDataReportDto(dto);
    return dataReportService.findRechReport(dto);
  }

  @ApiOperation("提现数据统计")
  @GetMapping("findWithReport")
  @PreAuthorize("hasAuthority('thirdParty:withReport:count')")
  public GameWithDataReportVO findWithReport(GameRWDataReportDto dto) {
    log.info("查询数据统计提现入参：{}", JSON.toJSONString(dto));
    editGameRwDataReportDto(dto);
    return dataReportService.findWithReport(dto);
  }

  @ApiOperation("游戏数据统计")
  @GetMapping("findGameDataReport")
  @PreAuthorize("hasAuthority('thirdParty:gameDataReport:count')")
  public GameDataReportVO findGameDataReport(GameRWDataReportDto dto) {
    log.info("查询数据统计游戏入参：{}", JSON.toJSONString(dto));
    editGameRwDataReportDto(dto);
    return dataReportService.findGameReport(dto);
  }

  @ApiOperation("会员数据统计")
  @GetMapping("findAccountDataReport")
  @PreAuthorize("hasAuthority('thirdParty:accountDataReport:count')")
  public GameAccountDataReportVo findAccountDataReport(
      Page<AccountReportVo> page, GameRWDataReportDto dto) {
    log.info("查询数据统注册入参：{}", JSON.toJSONString(dto));
    editGameRwDataReportSecondDto(dto);

    return dataReportService.findMemberReport(page, dto);
  }

  /** 红利 = 充值优惠 + 彩金 + VIP红利 + 活动红利 + 聊天室红包 VIP红利 = 周俸禄 + 月俸禄 + 升级奖励 + 生日礼金 + 每月红包 活动红利 = 活动彩金 */
  @ApiOperation("红利数据统计")
  @GetMapping("findDividendtDataReport")
  @PreAuthorize("hasAuthority('thirdParty:dividendtDataReport:count')")
  public GameDividendDataVo findDividendtDataReport(GameRWDataReportDto dto) {
    log.info("查询数据统计红利入参：{}", JSON.toJSONString(dto));
    editGameRwDataReportDto(dto);
    return dataReportService.findDividendtDataReport(dto);
  }

  @ApiOperation("查询会员金额")
  @GetMapping("findAccountReport")
  @PreAuthorize("hasAuthority('thirdParty:accountReport:balance')")
  public PageDtoVO<AccountReportVo> findAccountReport(
      Page<AccountReportVo> page, GameRWDataReportDto dto) {
    log.info("查询数据统计红利入参：{}", JSON.toJSONString(dto));
    editGameRwDataReportDto(dto);
    return dataReportService.findAccountReport(page, dto);
  }

  @ApiOperation("根据第三方查询第三方充值金额")
  @GetMapping("findThreeRech")
  @PreAuthorize("hasAuthority('thirdParty:threeRech:balance')")
  public List<ThreeRechReportVo> findThreeRech(GameRWDataReportDto dto) {
    log.info("查询数据统计红利入参：{}", JSON.toJSONString(dto));
    editGameRwDataReportSecondDto(dto);
    return dataReportService.findThreeRech(dto);
  }

  @ApiOperation(value = "代理总计")
  @GetMapping("findProxyData")
  @PreAuthorize("hasAuthority('thirdParty:proxyData:count')")
  public GameProxyDataVo findProxyData(GameRWDataReportDto dto) {
    log.info("查询数据统计红利入参：{}", JSON.toJSONString(dto));
    editGameRwDataReportSecondDto(dto);
    return dataReportService.findProxyData(dto);
  }

  private String getAccountSuperPath(String account) {
    return memberService.getSupperPath(account).orElseThrow(() -> new ServiceException("代理账号不存在"));
  }

  /**
   * 时分秒
   *
   * @param dto GameRWDataReportDto
   */
  public void editGameRwDataReportSecondDto(GameRWDataReportDto dto) {
    if (StringUtils.isEmpty(dto.getStartTime())) {
      String startTime = DateUtils.format(new Date());
      startTime += " 00:00:00";
      dto.setStartTime(startTime);
    } else {
      dto.setStartTime(dto.getStartTime() + " 00:00:00");
    }
    if (StringUtils.isEmpty(dto.getEndTime())) {
      String endTime = DateUtils.format(new Date());
      endTime += " 23:59:59";
      dto.setEndTime(endTime);
    } else {
      dto.setEndTime(dto.getEndTime() + " 23:59:59");
    }
    if (StringUtils.isNotEmpty(dto.getSuperAccount())) {
      if (dto.getFlag() == ONE) {
        dto.setSuperAccount(this.getAccountSuperPath(dto.getSuperAccount()));
      }
    }
  }

  /**
   * 日期
   *
   * @param dto GameRWDataReportDto
   */
  public void editGameRwDataReportDto(GameRWDataReportDto dto) {
    if (StringUtils.isEmpty(dto.getStartTime())) {
      String startTime = DateUtils.format(new Date());
      dto.setStartTime(startTime);
    }
    if (StringUtils.isEmpty(dto.getEndTime())) {
      String endTime = DateUtils.format(new Date());
      dto.setEndTime(endTime);
    }
    if (StringUtils.isNotEmpty(dto.getSuperAccount())) {
      if (dto.getFlag() == ONE) {
        dto.setSuperAccount(this.getAccountSuperPath(dto.getSuperAccount()));
      }
    }
  }
}
