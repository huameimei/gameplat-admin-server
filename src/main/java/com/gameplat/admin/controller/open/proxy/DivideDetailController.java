package com.gameplat.admin.controller.open.proxy;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.DivideDetailQueryDTO;
import com.gameplat.admin.model.vo.DivideDetailVO;
import com.gameplat.admin.service.DivideDetailService;
import com.gameplat.model.entity.proxy.DivideDetail;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description : 层层代分红详情 @Author : cc @Date : 2022/4/2
 */
@Tag(name = "分红详情")
@RestController
@RequestMapping("/api/admin/divide/detail")
public class DivideDetailController {

  @Autowired private DivideDetailService divideDetailService;

  /**
   * 分页列表
   *
   * @param page
   * @param dto
   * @return
   */
  @Operation(summary = "分红详情")
  @GetMapping("/list")
  public IPage<DivideDetailVO> list(PageDTO<DivideDetail> page, DivideDetailQueryDTO dto) {
    return divideDetailService.queryPage(page, dto);
  }
}
