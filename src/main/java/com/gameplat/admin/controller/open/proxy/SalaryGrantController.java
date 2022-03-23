package com.gameplat.admin.controller.open.proxy;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.SalaryGrantDTO;
import com.gameplat.admin.model.vo.SalaryGrantVO;
import com.gameplat.admin.service.DividePeriodsService;
import com.gameplat.admin.service.SalaryGrantService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.proxy.SalaryGrant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "分红期数")
@RestController
@RequestMapping("/api/admin/salary/grant")
public class SalaryGrantController {

  @Autowired private DividePeriodsService periodsService;

  @Autowired private SalaryGrantService salaryGrantService;

  @ApiOperation(value = "工资统计")
  @GetMapping("/list")
  public IPage<SalaryGrantVO> list(PageDTO<SalaryGrant> page, SalaryGrantDTO dto) {
    return salaryGrantService.queryPage(page, dto);
  }

  @PostMapping("/change")
  @ApiOperation(value = "期数结算")
  @PreAuthorize("hasAuthority('salary:grant:change')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.AGENT, desc = "期数结算")
  public void change(@Validated @RequestBody SalaryGrantDTO dto) {
    salaryGrantService.change(dto.getId(), dto.getSalaryAmount());
  }
}
