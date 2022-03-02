package com.gameplat.admin.controller.open.proxy;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.model.entity.proxy.DivideDetail;
import com.gameplat.admin.model.dto.DivideDetailQueryDTO;
import com.gameplat.admin.model.vo.DivideDetailVO;
import com.gameplat.admin.service.DivideDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "分红详情")
@RestController
@RequestMapping("/api/admin/divide/detail")
@SuppressWarnings("all")
public class DivideDetailController {
    @Autowired
    private DivideDetailService divideDetailService;

    @ApiOperation(value = "分红详情")
    @GetMapping("/list")
    public IPage<DivideDetailVO> list(PageDTO<DivideDetail> page, DivideDetailQueryDTO dto) {
        return divideDetailService.queryPage(page, dto);
    }
}
