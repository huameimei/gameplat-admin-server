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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * banner管理
 *
 * @author kenvin
 */
@Api(tags = "banner管理")
@Slf4j
@RestController
@RequestMapping("/api/admin/system/banner")
public class OpenBannerController {

  @Autowired private SysBannerInfoService sysBannerInfoService;

    /**
     * banner列表
     *
     * @param page
     * @param language
     * @return
     */
    @ApiOperation(value = "banner列表")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('system:banner:page')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "分页参数：当前页", defaultValue = "1"),
            @ApiImplicitParam(name = "size", value = "每页条数"),
            @ApiImplicitParam(name = "language", value = "语种"),
            @ApiImplicitParam(name = "type", value = "banner大类，1 体育banner配置，2 彩票banner配置")
    })
    public IPage<SysBannerInfoVO> list(
            @ApiIgnore PageDTO<SysBannerInfo> page,
            @RequestParam(defaultValue = "zh-CN") String language, Integer type) {
        return sysBannerInfoService.list(page, LocaleContextHolder.getLocale().toLanguageTag(), type);
    }

  /**
   * 新增banner
   *
   * @param sysBannerInfoAddDTO
   */
  @ApiOperation(value = "新增banner")
  @PostMapping("/add")
  @PreAuthorize("hasAuthority('system:banner:add')")
  public void add(@Validated @RequestBody SysBannerInfoAddDTO sysBannerInfoAddDTO) {
    sysBannerInfoService.add(sysBannerInfoAddDTO);
  }

  /**
   * 编辑banner
   *
   * @param sysBannerInfoEditDTO
   */
  @ApiOperation(value = "编辑banner")
  @PutMapping("/edit")
  @PreAuthorize("hasAuthority('system:banner:edit')")
  public void edit(@Validated @RequestBody SysBannerInfoEditDTO sysBannerInfoEditDTO) {
    sysBannerInfoService.edit(sysBannerInfoEditDTO);
  }

  /**
   * 删除banner
   *
   * @param ids
   */
  @ApiOperation(value = "删除banner")
  @DeleteMapping("/delete")
  @PreAuthorize("hasAuthority('system:banner:remove')")
  public void delete(@RequestBody String ids) {
    Assert.notEmpty(ids, "ids不能为空");
    sysBannerInfoService.delete(ids);
  }

  /**
   * 修改banner状态
   *
   * @param dto
   */
  @ApiOperation(value = "修改banner状态")
  @PutMapping("/updateStatus")
  @PreAuthorize("hasAuthority('system:banner:edit')")
  public void updateStatus(@Validated @RequestBody SysBannerInfoUpdateStatusDTO dto) {
    sysBannerInfoService.updateStatus(dto);
  }
}
