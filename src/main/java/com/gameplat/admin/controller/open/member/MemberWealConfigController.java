package com.gameplat.admin.controller.open.member;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.MemberWealConfigAddDTO;
import com.gameplat.admin.model.dto.MemberWealConfigEditDTO;
import com.gameplat.admin.service.MemberWealConfigService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.member.MemberWealConfig;
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
@Slf4j
@Tag(name = "VIP权益配置")
@RestController
@RequestMapping("/api/admin/member/wealConfig")
public class MemberWealConfigController {

  @Autowired private MemberWealConfigService memberWealConfigService;

  @PostMapping("/add")
  @Operation(summary = "新增权益配置")
  @PreAuthorize("hasAuthority('member:wealConfig:add')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'会员备注-->新增权益配置:' + #dto" )
  public void addWealConfig(@Validated MemberWealConfigAddDTO dto) {
    memberWealConfigService.addWealConfig(dto);
  }

  @Operation(summary = "删除权益配置")
  @PostMapping("/remove/{id}")
  @PreAuthorize("hasAuthority('member:wealConfig:remove')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'会员备注-->删除权益配置:' + #id" )
  public void removeWealConfig(@PathVariable Long id) {
    memberWealConfigService.removeWealConfig(id);
  }

  @PostMapping("/edit")
  @Operation(summary = "修改权益配置")
  @PreAuthorize("hasAuthority('member:wealConfig:edit')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'会员备注-->修改权益配置:' + #dto" )
  public void updateBanner(@Validated MemberWealConfigEditDTO dto) {
    memberWealConfigService.updateWealConfig(dto);
  }

  @GetMapping("/page")
  @Operation(summary = "查询权益列表")
  @PreAuthorize("hasAuthority('member:wealConfig:view')")
  public IPage<MemberWealConfig> page(PageDTO<MemberWealConfig> page) {
    return memberWealConfigService.page(page);
  }
}
