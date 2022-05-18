package com.gameplat.admin.controller.open.proxy;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.DividePeriodsDTO;
import com.gameplat.admin.model.dto.DividePeriodsQueryDTO;
import com.gameplat.admin.model.vo.DividePeriodsVO;
import com.gameplat.admin.service.DividePeriodsService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.proxy.DividePeriods;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Description : 层层代分红期数 @Author : cc @Date : 2022/4/2
 */
@Tag(name = "分红期数")
@RestController
@RequestMapping("/api/admin/divide/periods")
public class DividePeriodsController {

  @Autowired private DividePeriodsService periodsService;

  /**
   * 期数分页列表
   *
   * @param page
   * @param dto
   * @return
   */
  @Operation(summary = "期数列表")
  @GetMapping("/list")
  @PreAuthorize("hasAuthority('divide:periods:view')")
  public IPage<DividePeriodsVO> list(PageDTO<DividePeriods> page, DividePeriodsQueryDTO dto) {
    return periodsService.queryPage(page, dto);
  }

  /**
   * 添加期数
   *
   * @param dto
   */
  @PostMapping("/add")
  @Operation(summary = "新增期数")
  @PreAuthorize("hasAuthority('divide:periods:add')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.AGENT, desc = "新增期数")
  public void add(@Validated @RequestBody DividePeriodsDTO dto) {
    periodsService.add(dto);
  }

  /**
   * 编辑
   *
   * @param dto
   */
  @PostMapping("/edit")
  @Operation(summary = "编辑期数")
  @PreAuthorize("hasAuthority('divide:periods:edit')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.AGENT, desc = "编辑期数")
  public void edit(@Validated @RequestBody DividePeriodsDTO dto) {
    periodsService.edit(dto);
  }

  /**
   * 删除
   *
   * @param map
   */
  @Operation(summary = "删除期数")
  @PostMapping("/delete")
  @PreAuthorize("hasAuthority('divide:periods:remove')")
  public void remove(@RequestBody Map<String, String> map) {
    if (StringUtils.isBlank(map.get("ids"))) {
      throw new ServiceException("ids不能为空");
    }
    periodsService.delete(map.get("ids"));
  }

  /**
   * 结算
   *
   * @param dto
   */
  @PostMapping("/settle")
  @Operation(summary = "期数结算")
  @PreAuthorize("hasAuthority('divide:periods:settle')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.AGENT, desc = "期数结算")
  public void settle(@Validated @RequestBody DividePeriodsDTO dto) {
    periodsService.settle(dto);
  }

  /**
   * 派发
   *
   * @param dto
   */
  @PostMapping("/grant")
  @Operation(summary = "期数派发")
  @PreAuthorize("hasAuthority('divide:periods:grant')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.AGENT, desc = "期数派发")
  public void grant(@Validated @RequestBody DividePeriodsDTO dto) {
    periodsService.grant(dto);
  }

  /**
   * 回收
   *
   * @param dto
   */
  @PostMapping("/recycle")
  @Operation(summary = "期数回收")
  @PreAuthorize("hasAuthority('divide:periods:recycle')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.AGENT, desc = "期数回收")
  public void recycle(@Validated @RequestBody DividePeriodsDTO dto) {
    periodsService.recycle(dto);
  }
}
