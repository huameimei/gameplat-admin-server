package com.gameplat.admin.controller.open.proxy;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.DivideSummaryQueryDTO;
import com.gameplat.admin.model.vo.DivideSummaryVO;
import com.gameplat.admin.service.DivideSummaryService;
import com.gameplat.model.entity.proxy.DivideSummary;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** @Description : 层层代分红汇总 @Author : cc @Date : 2022/4/2 */
@Api(tags = "分红汇总")
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
  @ApiOperation(value = "分红汇总")
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
  @ApiOperation(value = "获取最大层级")
  @GetMapping("/getMaxLevel")
  @PreAuthorize("hasAuthority('divide:summary:max')")
  public Integer getMaxLevel(DivideSummaryQueryDTO dto) {
    return summaryService.getMaxLevel(dto);
  }
}
