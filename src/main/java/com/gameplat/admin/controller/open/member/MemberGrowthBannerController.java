package com.gameplat.admin.controller.open.member;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.MemberGrowthBannerAddDTO;
import com.gameplat.admin.model.dto.MemberGrowthBannerEditDTO;
import com.gameplat.admin.model.dto.MemberGrowthBannerQueryDTO;
import com.gameplat.admin.model.vo.MemberGrowthBannerVO;
import com.gameplat.admin.service.MemberGrowthBannerService;
import com.gameplat.model.entity.member.MemberGrowthBanner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * VIP轮播图配置
 *
 * @author lily
 */
@Api(tags = "VIP轮播图配置")
@Slf4j
@RestController
@RequestMapping("/api/admin/member/banner")
public class MemberGrowthBannerController {

  @Autowired private MemberGrowthBannerService memberGrowthBannerService;

  @PostMapping("/add")
  @ApiOperation("新增banner图")
  @PreAuthorize("hasAuthority('member:banner:add')")
  public void addBanner(@Validated MemberGrowthBannerAddDTO dto) {
    memberGrowthBannerService.addBanner(dto);
  }

  @ApiOperation("删除VIP banner图")
  @DeleteMapping("/remove/{id}")
  @PreAuthorize("hasAuthority('member:banner:remove')")
  public void removeBanner(@PathVariable Long id) {
    memberGrowthBannerService.remove(id);
  }

  @PutMapping("/edit")
  @ApiOperation("修改VIP banner图")
  @PreAuthorize("hasAuthority('member:banner:edit')")
  public void updateBanner(@Validated MemberGrowthBannerEditDTO dto) {
    memberGrowthBannerService.updateBanner(dto);
  }

  @GetMapping("/page")
  @ApiOperation("VIP banner图列表")
  @PreAuthorize("hasAuthority('member:banner:view')")
  public IPage<MemberGrowthBannerVO> findTrendsList(
      PageDTO<MemberGrowthBanner> page, MemberGrowthBannerQueryDTO dto) {
    return memberGrowthBannerService.getList(page, dto);
  }
}
