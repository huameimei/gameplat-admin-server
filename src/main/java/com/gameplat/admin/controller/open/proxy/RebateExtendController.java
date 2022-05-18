package com.gameplat.admin.controller.open.proxy;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.service.RebateExtendService;
import com.gameplat.model.entity.proxy.RebateReportExtend;
import com.gameplat.security.SecurityUserHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description : 平级分红方案佣金调整 @Author : cc @Date : 2022/4/2
 */
@Tag(name = "平级分红方案佣金调整")
@Slf4j
@RestController
@RequestMapping("/api/admin/same-level/extend")
public class RebateExtendController {
  @Autowired private RebateExtendService rebateExtendService;

  /**
   * 分页列表
   *
   * @param page
   * @param reportId
   * @return
   */
  @Operation(summary = "查询佣金调整记录")
  @GetMapping("/list")
  public IPage<RebateReportExtend> reportExtendList(
      PageDTO<RebateReportExtend> page, @RequestParam Long reportId) {
    log.info("查询佣金调整记录：reportId={}", reportId);
    return rebateExtendService.queryPage(page, reportId);
  }

  /**
   * 新增
   *
   * @param extendPO
   */
  @Operation(summary = "新增佣金调整记录")
  @PostMapping(value = "/add")
  public void addReportExtend(@RequestBody RebateReportExtend extendPO) {
    log.info("新增佣金调整记录：extendPO={}", extendPO);
    extendPO.setUpdateBy(SecurityUserHolder.getUsername());
    rebateExtendService.addReportExtend(extendPO);
  }

  /**
   * 编辑
   *
   * @param extendPO
   */
  @Operation(summary = "编辑佣金调整记录")
  @PostMapping(value = "/edit")
  public void editReportExtend(@RequestBody RebateReportExtend extendPO) {
    log.info("编辑佣金调整记录：extendPO={}", extendPO);
    extendPO.setUpdateBy(SecurityUserHolder.getUsername());
    rebateExtendService.editReportExtend(extendPO);
  }

  /**
   * 删除
   *
   * @param extendPO
   */
  @Operation(summary = "删除佣金调整记录")
  @PostMapping(value = "/remove")
  public void removeReportExtend(@RequestBody RebateReportExtend extendPO) {
    extendPO.setUpdateBy(SecurityUserHolder.getUsername());
    rebateExtendService.removeReportExtend(extendPO);
  }
}
