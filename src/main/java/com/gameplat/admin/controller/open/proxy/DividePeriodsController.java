package com.gameplat.admin.controller.open.proxy;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.DividePeriodsDTO;
import com.gameplat.admin.model.dto.DividePeriodsQueryDTO;
import com.gameplat.admin.model.vo.DividePeriodsVO;
import com.gameplat.admin.service.DividePeriodsService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.proxy.DividePeriods;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "分红期数")
@RestController
@RequestMapping("/api/admin/divide/periods")
public class DividePeriodsController {

  @Autowired private DividePeriodsService periodsService;

  @ApiOperation(value = "期数列表")
  @GetMapping("/list")
  public IPage<DividePeriodsVO> list(PageDTO<DividePeriods> page, DividePeriodsQueryDTO dto) {
    return periodsService.queryPage(page, dto);
  }

  @PostMapping("/add")
  @ApiOperation(value = "新增期数")
  @PreAuthorize("hasAuthority('divide:periods:add')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.AGENT, desc = "新增期数")
  public void add(@Validated @RequestBody DividePeriodsDTO dto) {
    periodsService.add(dto);
  }

  @PostMapping("/edit")
  @ApiOperation(value = "编辑期数")
  @PreAuthorize("hasAuthority('divide:periods:edit')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.AGENT, desc = "编辑期数")
  public void edit(@Validated @RequestBody DividePeriodsDTO dto) {
    periodsService.edit(dto);
  }

  @ApiOperation(value = "删除期数")
  @DeleteMapping("/delete")
  @PreAuthorize("hasAuthority('divide:periods:del')")
  public void remove(@RequestBody String ids) {
    periodsService.delete(ids);
  }

  @PostMapping("/settle")
  @ApiOperation(value = "期数结算")
  @PreAuthorize("hasAuthority('divide:periods:settle')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.AGENT, desc = "期数结算")
  public void settle(@Validated @RequestBody DividePeriodsDTO dto) {
    periodsService.settle(dto);
  }

  @PostMapping("/grant")
  @ApiOperation(value = "期数派发")
  @PreAuthorize("hasAuthority('divide:periods:grant')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.AGENT, desc = "期数派发")
  public void grant(@Validated @RequestBody DividePeriodsDTO dto) {
    periodsService.grant(dto);
  }

  @PostMapping("/recycle")
  @ApiOperation(value = "期数回收")
  @PreAuthorize("hasAuthority('divide:periods:recycle')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.AGENT, desc = "期数回收")
  public void recycle(@Validated @RequestBody DividePeriodsDTO dto) {
    periodsService.recycle(dto);
  }
}
