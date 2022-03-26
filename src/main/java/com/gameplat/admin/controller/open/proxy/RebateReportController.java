package com.gameplat.admin.controller.open.proxy;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.RebateReportDTO;
import com.gameplat.admin.model.vo.*;
import com.gameplat.admin.service.RebateReportService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.BeanUtils;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.constant.NumberConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Api(tags = "平级分红方案佣金报表")
@Slf4j
@RestController
@RequestMapping("/api/admin/same-level/report")
public class RebateReportController {
  @Autowired private RebateReportService rebateReportService;

  @ApiOperation(value = "平级分红报表")
  @GetMapping("/list")
  public IPage<RebateReportVO> list(PageDTO<AgentPlanVO> page, RebateReportDTO rebateReportDTO) {
    // todo 参数初始化
    if (StrUtil.isBlank(rebateReportDTO.getCountDate())) {
      rebateReportDTO.setCountDate(DateUtil.format(DateTime.now(), "yyyy-MM"));
    }
    if (Objects.isNull(rebateReportDTO.getAccountStatus())) {
      rebateReportDTO.setAccountStatus(-1);
    }
    if (Objects.isNull(rebateReportDTO.getStatus())) {
      rebateReportDTO.setStatus(-1);
    }
    if (Objects.isNull(rebateReportDTO.getPlanId())) {
      rebateReportDTO.setPlanId(-1L);
    }
    if (Objects.isNull(rebateReportDTO.getLevelNum())) {
      rebateReportDTO.setLevelNum(-1);
    }
    if (Objects.isNull(rebateReportDTO.getActualCommissionLeast())) {
      rebateReportDTO.setActualCommissionLeast(BigDecimal.ZERO);
    }
    if (Objects.isNull(rebateReportDTO.getActualCommissionMost())) {
      rebateReportDTO.setActualCommissionMost(BigDecimal.ZERO);
    }
    log.info("查询平级分红方案报表：rebateReportDTO={}", rebateReportDTO);
    return rebateReportService.queryPage(page, rebateReportDTO);
  }

  @ApiOperation("导出")
  @GetMapping(value = "/export")
  public void export(
      RebateReportDTO rebateReportDTO, HttpServletRequest request, HttpServletResponse response) {
    // todo 参数初始化
    if (StringUtils.isEmpty(rebateReportDTO.getCountDate())) {
      rebateReportDTO.setCountDate(DateUtil.format(DateTime.now(), "yyyy-MM"));
    }
    if (Objects.isNull(rebateReportDTO.getAccountStatus())) {
      rebateReportDTO.setAccountStatus(-1);
    }
    if (Objects.isNull(rebateReportDTO.getStatus())) {
      rebateReportDTO.setStatus(-1);
    }
    if (Objects.isNull(rebateReportDTO.getPlanId())) {
      rebateReportDTO.setPlanId(-1L);
    }
    if (Objects.isNull(rebateReportDTO.getLevelNum())) {
      rebateReportDTO.setLevelNum(-1);
    }
    if (Objects.isNull(rebateReportDTO.getActualCommissionLeast())) {
      rebateReportDTO.setActualCommissionLeast(BigDecimal.ZERO);
    }
    if (Objects.isNull(rebateReportDTO.getActualCommissionMost())) {
      rebateReportDTO.setActualCommissionMost(BigDecimal.ZERO);
    }
    log.info("导出平级分红方案报表：rebateReportDTO={}", rebateReportDTO);
    List<RebateReportVO> list;
    List<RebateReportExcelVO> excelVOS;
    try {
      list = rebateReportService.getRebateReport(rebateReportDTO);
      excelVOS = BeanUtils.mapList(list, RebateReportExcelVO.class);
      String fileName = rebateReportDTO.getCountDate() + "佣金报表";
      fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
      response.setContentType("application/vnd.ms-excel");
      response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xls");
      @Cleanup OutputStream outputStream = null;
      Workbook workbook =
          ExcelExportUtil.exportExcel(
              new ExportParams(rebateReportDTO.getCountDate().concat("佣金报表"), "佣金报表"),
              RebateReportExcelVO.class,
              excelVOS);
      outputStream = response.getOutputStream();
      workbook.write(outputStream);
    } catch (Exception e) {
      log.info("异常原因：{}", e);
      throw new ServiceException(e);
    }
  }

  @ApiOperation("更新佣金报表")
  @PostMapping(value = "/updateReport")
  public void updateReport(
      @RequestParam(required = false) String agentName, @RequestParam String countDate) {
    rebateReportService.updateRebateReport(countDate, agentName);
  }

  @ApiOperation(value = "查询会员报表")
  @GetMapping("/memberList")
  public IPage<MemberReportVO> memberList(
      PageDTO<MemberReportVO> page, @RequestParam Long agentId, @RequestParam String countDate) {
    return rebateReportService.pageMemberReport(page, agentId, countDate);
  }

  @ApiOperation(value = "查询公司总输赢")
  @GetMapping("/gameWin")
  public PageDtoVO<PlatformFeeVO> gameWin(
      PageDTO<PlatformFeeVO> page, @RequestParam Long agentId, @RequestParam String countDate) {

    PageDtoVO<PlatformFeeVO> pageDtoVO = new PageDtoVO<>();
    GameWinVO gameWinVO = rebateReportService.getPlatformFeeSum(agentId, countDate);
    Page<PlatformFeeVO> platformFeeVOIPage = rebateReportService.gameWin(page, agentId, countDate);
    Map<String, Object> otherData = new HashMap<>();
    otherData.put("totalData", gameWinVO);
    pageDtoVO.setPage(platformFeeVOIPage);
    pageDtoVO.setOtherData(otherData);
    return pageDtoVO;
  }

  //  @ApiOperation(value = "查询公司总成本")
  //  @GetMapping("/totalCost")
  //  public PageDtoVO<CompanyCostVO> totalCost(
  //          PageDTO<MemberReportVO> page, @RequestParam Long agentId, @RequestParam String
  // countDate) {
  //    PageDtoVO<CompanyCostVO> pageDtoVO = new PageDtoVO<>();
  //    List<CompanyCostVO> list = rebateReportService.pagePlatformCost(agentId, countDate);
  //    GameWinVO gameWinVO = rebateReportService.getPlatformFeeSum(agentId, countDate);
  //    Map<String, Object> otherData = new HashMap<>();
  //    otherData.put("totalData", gameWinVO);
  //    pageDtoVO.setPage(new Page<>(list));
  //    pageDtoVO.setOtherData(otherData);
  //    return pageDtoVO;
  //  }

  @ApiOperation(value = "查询公司总成本")
  @GetMapping("/totalCost")
  public Map<String, Object> totalCost(@RequestParam Long agentId, @RequestParam String countDate) {
    Map<String, Object> returnMap = new HashMap<>();
    GameWinVO gameWinVO = rebateReportService.getPlatformFeeSum(agentId, countDate);
    returnMap.put("otherData", gameWinVO);
    List<CompanyCostVO> list = rebateReportService.pagePlatformCost(agentId, countDate);
    returnMap.put("list", list);
    return returnMap;
  }

  @ApiOperation("下级会员佣金")
  @GetMapping(value = "/memberCommission")
  public MemberCommissionVO memberCommission(
      @RequestParam Long agentId, @RequestParam String countDate) {
    log.info("下级会员佣金：agentId={}，countDate={}", agentId, countDate);
    return rebateReportService.getMemberCommission(agentId, countDate);
  }

  @ApiOperation("下级代理佣金")
  @GetMapping(value = "/agentCommission")
  public PageDtoVO<AgentCommissionVO> agentCommission(
      PageDTO<AgentCommissionVO> page, @RequestParam Long agentId, @RequestParam String countDate) {
    log.info("下级代理佣金：agentId={}，countDate={}", agentId, countDate);
    AgentCommissionVO agentCommissionVO =
        rebateReportService.getAgentCommission(agentId, countDate);
    Page<AgentCommissionVO> pageList =
        rebateReportService.getSubAgentCommission(page, agentId, countDate);

    PageDtoVO<AgentCommissionVO> pageDtoVO = new PageDtoVO<>();
    Map<String, Object> otherData = new HashMap<>();
    otherData.put("totalData", agentCommissionVO);
    pageDtoVO.setPage(pageList);
    pageDtoVO.setOtherData(otherData);
    return pageDtoVO;
  }

  @ApiOperation("风控审核")
  @PostMapping(value = "/riskControlAudit")
  public void riskControlAudit(@RequestParam Long reportId) {
    log.info("风控审核：reportId={}", reportId);
    rebateReportService.reviewOrSettlement(NumberConstant.ONE, reportId);
  }

  @ApiOperation("财务审核")
  @PostMapping(value = "/financialAudit")
  public void financialAudit(@RequestParam Long reportId) {
    log.info("财务审核：reportId={}", reportId);
    rebateReportService.reviewOrSettlement(NumberConstant.TWO, reportId);
  }

  @ApiOperation("结算")
  @PostMapping(value = "/settlement")
  public void settlement(@RequestParam Long reportId) {
    log.info("结算：reportId={}", reportId);
    rebateReportService.reviewOrSettlement(NumberConstant.THREE, reportId);
  }

  @ApiOperation("批量风控审核")
  @PostMapping(value = "/batchRiskControlAudit")
  public void batchRiskControlAudit(@RequestParam String countDate) {
    log.info("批量风控审核：countDate={}", countDate);
    rebateReportService.batchReviewOrSettlement(NumberConstant.ZERO, countDate);
  }

  @ApiOperation("批量财务审核")
  @PostMapping(value = "/batchFinancialAudit")
  public void batchFinancialAudit(@RequestParam String countDate) {
    log.info("批量财务审核：countDate={}", countDate);
    rebateReportService.batchReviewOrSettlement(NumberConstant.ONE, countDate);
  }

  @ApiOperation("批量结算")
  @PostMapping(value = "/batchSettlement")
  public void batchSettlement(@RequestParam String countDate) {
    log.info("批量结算：countDate={}", countDate);
    rebateReportService.batchReviewOrSettlement(NumberConstant.TWO, countDate);
  }
}
