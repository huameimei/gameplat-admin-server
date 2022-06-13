package com.gameplat.admin.controller.open.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.SysBannerInfoAddDTO;
import com.gameplat.admin.model.dto.SysBannerInfoEditDTO;
import com.gameplat.admin.model.dto.SysBannerInfoUpdateStatusDTO;
import com.gameplat.admin.model.vo.SysBannerInfoVO;
import com.gameplat.admin.service.SysBannerInfoService;
import com.gameplat.common.lang.Assert;
import com.gameplat.model.entity.sys.SysBannerInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * banner管理
 *
 * @author kenvin
 */
@Tag(name = "banner管理")
@RestController
@RequestMapping("/api/admin/system/banner")
public class OpenBannerController {

  @Autowired private SysBannerInfoService sysBannerInfoService;

  @Operation(summary = "banner列表")
  @GetMapping("/list")
  @PreAuthorize("hasAuthority('system:banner:view')")
  public IPage<SysBannerInfoVO> list(
      @Parameter(hidden = true) PageDTO<SysBannerInfo> page, Integer type) {
    return sysBannerInfoService.list(page, LocaleContextHolder.getLocale().toLanguageTag(), type);
  }

  @Operation(summary = "新增banner")
  @PostMapping("/add")
  @PreAuthorize("hasAuthority('system:banner:add')")
  public void add(@Validated @RequestBody SysBannerInfoAddDTO dto) {
    sysBannerInfoService.add(dto);
  }

  @Operation(summary = "编辑banner")
  @PostMapping("/edit")
  @PreAuthorize("hasAuthority('system:banner:edit')")
  public void edit(@Validated @RequestBody SysBannerInfoEditDTO dto) {
    sysBannerInfoService.edit(dto);
  }

  @Operation(summary = "删除banner")
  @PostMapping("/delete")
  @PreAuthorize("hasAuthority('system:banner:remove')")
  public void delete(@RequestBody Map<String, String> map) {
    Assert.notEmpty(map.get("ids"), "ids不能为空");
    sysBannerInfoService.delete(map.get("ids"));
  }

  @Operation(summary = "修改banner状态")
  @PostMapping("/updateStatus")
  @PreAuthorize("hasAuthority('system:banner:edit')")
  public void updateStatus(@Validated @RequestBody SysBannerInfoUpdateStatusDTO dto) {
    sysBannerInfoService.updateStatus(dto);
  }
}
