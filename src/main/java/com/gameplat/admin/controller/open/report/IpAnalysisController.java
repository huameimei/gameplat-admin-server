package com.gameplat.admin.controller.open.report;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.IpAnalysisDTO;
import com.gameplat.admin.model.vo.IpAnalysisVO;
import com.gameplat.admin.service.IpAnalysisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ip分析
 *
 * @author lily
 */
@Tag(name = "IP分析报表")
@RestController
@RequestMapping("/api/admin/report/ip")
public class IpAnalysisController {

  @Autowired private IpAnalysisService ipAnalysisService;

  @GetMapping(value = "/page")
  @Operation(summary = "IP分析报表列表")
  @PreAuthorize("hasAuthority('ip:analysis:view')")
  public IPage<IpAnalysisVO> page(PageDTO<IpAnalysisVO> page, IpAnalysisDTO dto) {
    return ipAnalysisService.page(page, dto);
  }
}
