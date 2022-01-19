package com.gameplat.admin.controller.open.report;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.IpAnalysisDTO;
import com.gameplat.admin.model.vo.IpAnalysisVO;
import com.gameplat.admin.service.IpAnalysisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author lily
 * @description ip分析
 * @date 2022/1/18
 */
@Slf4j
@Api(tags = "IP分析报表")
@RestController
@RequestMapping("/api/admin/report/ip")
public class IpAnalysisController {

  @Autowired
  private IpAnalysisService ipAnalysisService;

  @GetMapping(value = "/page")
  @ApiOperation(value = "IP分析报表列表")
  @PreAuthorize("hasAuthority('ip:analysis:page')")
  public IPage<IpAnalysisVO> page(PageDTO<IpAnalysisVO> page, IpAnalysisDTO dto) {
      return ipAnalysisService.page(page, dto);
  }

}
