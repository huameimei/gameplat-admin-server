package com.gameplat.admin.controller.open.member;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.MemberGrowthBannerAddDTO;
import com.gameplat.admin.model.dto.MemberGrowthBannerEditDTO;
import com.gameplat.admin.model.dto.MemberGrowthBannerQueryDTO;
import com.gameplat.admin.model.vo.MemberGrowthBannerVO;
import com.gameplat.admin.service.MemberGrowthBannerService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.member.MemberGrowthBanner;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "VIP轮播图配置")
@Slf4j
@RestController
@RequestMapping("/api/admin/member/banner")
public class MemberGrowthBannerController {

  @Autowired private MemberGrowthBannerService memberGrowthBannerService;

  @PostMapping("/add")
  @Operation(summary = "新增banner图")
  @PreAuthorize("hasAuthority('member:banner:add')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'VIP轮播图配置-->新增banner图:' + #dto" )
  public void addBanner(@Validated MemberGrowthBannerAddDTO dto) {
    memberGrowthBannerService.addBanner(dto);
  }

  @Operation(summary = "删除VIP banner图")
  @PostMapping("/remove/{id}")
  @PreAuthorize("hasAuthority('member:banner:remove')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'VIP轮播图配置-->删除VIP banner图:' + #id" )
  public void removeBanner(@PathVariable Long id) {
    memberGrowthBannerService.remove(id);
  }

  @PostMapping("/edit")
  @Operation(summary = "修改VIP banner图")
  @PreAuthorize("hasAuthority('member:banner:edit')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'VIP轮播图配置-->修改VIP banner图:' + #dto" )
  public void updateBanner(@Validated MemberGrowthBannerEditDTO dto) {
    memberGrowthBannerService.updateBanner(dto);
  }

  @GetMapping("/page")
  @Operation(summary = "VIP banner图列表")
  @PreAuthorize("hasAuthority('member:banner:view')")
  public IPage<MemberGrowthBannerVO> findTrendsList(
      PageDTO<MemberGrowthBanner> page, MemberGrowthBannerQueryDTO dto) {
    return memberGrowthBannerService.getList(page, dto);
  }
}
