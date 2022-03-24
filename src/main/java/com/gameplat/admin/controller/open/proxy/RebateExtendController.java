package com.gameplat.admin.controller.open.proxy;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.service.RebateExtendService;
import com.gameplat.model.entity.proxy.RebateReportExtend;
import com.gameplat.security.SecurityUserHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "平级分红方案佣金调整")
@Slf4j
@RestController
@RequestMapping("/api/admin/same-level/extend")
public class RebateExtendController {
  @Autowired private RebateExtendService rebateExtendService;

  @ApiOperation(value = "查询佣金调整记录")
  @GetMapping("/list")
  public IPage<RebateReportExtend> reportExtendList(
      PageDTO<RebateReportExtend> page, @RequestParam Long reportId) {
    log.info("查询佣金调整记录：reportId={}", reportId);
    return rebateExtendService.queryPage(page, reportId);
  }

  @ApiOperation("新增佣金调整记录")
  @PostMapping(value = "/add")
  public void addReportExtend(@RequestBody RebateReportExtend extendPO) {
    log.info("新增佣金调整记录：extendPO={}", extendPO);
    extendPO.setUpdateBy(SecurityUserHolder.getUsername());
    rebateExtendService.addReportExtend(extendPO);
  }

  @ApiOperation("编辑佣金调整记录")
  @PostMapping(value = "/edit")
  public void editReportExtend(@RequestBody RebateReportExtend extendPO) {
    log.info("编辑佣金调整记录：extendPO={}", extendPO);
    extendPO.setUpdateBy(SecurityUserHolder.getUsername());
    rebateExtendService.editReportExtend(extendPO);
  }

  @ApiOperation("删除佣金调整记录")
  @PostMapping(value = "/remove")
  public void removeReportExtend(@RequestParam Long extendId) {
    RebateReportExtend reportExtendPO =
        new RebateReportExtend() {
          {
            setExtendId(extendId);
            setUpdateBy(SecurityUserHolder.getUsername());
          }
        };
    rebateExtendService.removeReportExtend(reportExtendPO);
  }
}
