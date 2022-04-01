package com.gameplat.admin.controller.open.proxy;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.SalaryPeriodsDTO;
import com.gameplat.admin.model.vo.SalaryPeriodsVO;
import com.gameplat.admin.service.SalaryPeriodsService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.proxy.SalaryPeriods;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "分红期数")
@RestController
@RequestMapping("/api/admin/salary/periods")
public class SalaryPeriodsController {

  @Autowired private SalaryPeriodsService salaryPeriodsService;

  @ApiOperation(value = "期数列表")
  @GetMapping("/list")
  @PreAuthorize("hasAuthority('salary:periods:view')")
  public IPage<SalaryPeriodsVO> list(PageDTO<SalaryPeriods> page, SalaryPeriodsDTO dto) {
    return salaryPeriodsService.queryPage(page, dto);
  }

  @PostMapping("/add")
  @ApiOperation(value = "新增工资配置")
  @PreAuthorize("hasAuthority('salary:periods:add')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.AGENT, desc = "新增工资配置")
  public void add(@Validated @RequestBody SalaryPeriodsDTO dto) {
    salaryPeriodsService.add(dto);
  }

  @PostMapping("/edit")
  @ApiOperation(value = "编辑工资配置")
  @PreAuthorize("hasAuthority('salary:periods:edit')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.AGENT, desc = "编辑工资配置")
  public void edit(@Validated @RequestBody SalaryPeriodsDTO dto) {
    salaryPeriodsService.edit(dto);
  }

  @ApiOperation(value = "删除期数")
  @DeleteMapping("/delete")
  @PreAuthorize("hasAuthority('salary:periods:remove')")
  public void remove(@RequestBody String ids) {
    salaryPeriodsService.delete(ids);
  }

  @PostMapping("/settle")
  @ApiOperation(value = "期数结算")
  @PreAuthorize("hasAuthority('salary:periods:settle')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.AGENT, desc = "期数结算")
  public void settle(@Validated @RequestBody SalaryPeriodsDTO dto) {
    salaryPeriodsService.settle(dto.getId());
  }

  @PostMapping("/grant")
  @ApiOperation(value = "期数派发")
  @PreAuthorize("hasAuthority('salary:periods:grant')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.AGENT, desc = "期数派发")
  public void grant(@Validated @RequestBody SalaryPeriodsDTO dto) {
    salaryPeriodsService.grant(dto.getId());
  }

  @PostMapping("/recycle")
  @ApiOperation(value = "期数回收")
  @PreAuthorize("hasAuthority('salary:periods:recycle')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.AGENT, desc = "期数回收")
  public void recycle(@Validated @RequestBody SalaryPeriodsDTO dto) {
    salaryPeriodsService.recycle(dto.getId());
  }
}
