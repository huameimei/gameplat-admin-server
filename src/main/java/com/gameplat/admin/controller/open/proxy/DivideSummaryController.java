package com.gameplat.admin.controller.open.proxy;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.DivideSummaryQueryDTO;
import com.gameplat.admin.model.vo.DivideSummaryVO;
import com.gameplat.admin.service.DivideSummaryService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.proxy.DivideSummary;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @Description : 层层代分红汇总 @Author : cc @Date : 2022/4/2
 */
@Tag(name = "分红汇总")
@RestController
@RequestMapping("/api/admin/divide/summary")
public class DivideSummaryController {

  @Autowired private DivideSummaryService summaryService;

  /**
   * 分红汇总列表查询
   *
   * @param page
   * @param dto
   * @return
   */
  @Operation(summary = "分红汇总")
  @GetMapping("/list")
  @PreAuthorize("hasAuthority('divide:summary:view')")
  public IPage<DivideSummaryVO> list(PageDTO<DivideSummary> page, DivideSummaryQueryDTO dto) {
    return summaryService.queryPage(page, dto);
  }

  /**
   * 获取某期最大的代理等级
   *
   * @param dto
   * @return
   */
  @Operation(summary = "获取最大层级")
  @GetMapping("/getMaxLevel")
  @PreAuthorize("hasAuthority('divide:summary:max')")
  public Integer getMaxLevel(DivideSummaryQueryDTO dto) {
    return summaryService.getMaxLevel(dto);
  }

  @Operation(summary = "删除分红汇总")
  @PostMapping("/remove")
  @PreAuthorize("hasAuthority('divide:periods:remove')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'分红汇总-->删除分红汇总:' + #del")
  public void removePeriods(@RequestBody DivideSummary del) {
    if (del.getId() == null) {
      throw new ServiceException("参数缺失");
    }
    boolean b = summaryService.removeById(del.getId());
  }
}
