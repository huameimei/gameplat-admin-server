package com.gameplat.admin.controller.open.report;

import com.gameplat.admin.model.dto.HgSportWinReportQueryDTO;
import com.gameplat.admin.model.vo.HgSportWinReportVO;
import com.gameplat.admin.service.HgSportWinReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author aBen
 * @date 2022/4/17 19:28
 * @desc 皇冠输赢报表
 */
@Slf4j
@Api(tags = "皇冠输赢报表")
@RestController
@RequestMapping("/api/admin/report/hg")
public class HgSportWinReportController {

  @Autowired
  private HgSportWinReportService hgSportWinReportService;

  @GetMapping(value = "/findList")
  @PreAuthorize("hasAuthority('report:hg:view')")
  @ApiOperation(value = "查询皇冠输赢报表")
  public List<HgSportWinReportVO> findList(HgSportWinReportQueryDTO queryDTO) {
    return hgSportWinReportService.findList(queryDTO);
  }


}
