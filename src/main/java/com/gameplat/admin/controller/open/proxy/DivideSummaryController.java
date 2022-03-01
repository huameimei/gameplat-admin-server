package com.gameplat.admin.controller.open.proxy;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.proxy.DividePeriods;
import com.gameplat.admin.model.domain.proxy.DivideSummary;
import com.gameplat.admin.model.dto.DividePeriodsQueryDTO;
import com.gameplat.admin.model.dto.DivideSummaryQueryDTO;
import com.gameplat.admin.model.vo.DividePeriodsVO;
import com.gameplat.admin.model.vo.DivideSummaryVO;
import com.gameplat.admin.service.DividePeriodsService;
import com.gameplat.admin.service.DivideSummaryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "分红汇总")
@RestController
@RequestMapping("/api/admin/divide/summary")
@SuppressWarnings("all")
public class DivideSummaryController {
    @Autowired
    private DivideSummaryService summaryService;

    @ApiOperation(value = "分红汇总")
    @GetMapping("/list")
    public IPage<DivideSummaryVO> list(PageDTO<DivideSummary> page, DivideSummaryQueryDTO dto) {
        return summaryService.queryPage(page, dto);
    }

    @ApiOperation(value = "获取最大层级")
    @GetMapping("/getMaxLevel")
    public Integer getMaxLevel(DivideSummaryQueryDTO dto) {
        return summaryService.getMaxLevel(dto);
    }

}
