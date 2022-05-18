package com.gameplat.admin.controller.open.member;

import com.gameplat.admin.model.dto.MemberGrowthConfigEditDto;
import com.gameplat.admin.model.vo.MemberGrowthConfigVO;
import com.gameplat.admin.service.MemberGrowthConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * VIP说明配置
 *
 * @author lily
 */
@Tag(name = "VIP说明配置")
@Slf4j
@RestController
@RequestMapping("/api/admin/member/growthConfig")
public class MemberGrowthConfigController {

  @Autowired private MemberGrowthConfigService memberGrowthConfigService;

  @Operation(summary = "查询")
  @GetMapping("/get")
  @PreAuthorize("hasAuthority('member:growthConfig:view')")
  public MemberGrowthConfigVO getOne() {
    return memberGrowthConfigService.findOneConfig();
  }

  @Operation(summary = "修改")
  @PostMapping("/edit")
  @PreAuthorize("hasAuthority('member:growthConfig:edit')")
  public void update(@Validated MemberGrowthConfigEditDto dto) {
    memberGrowthConfigService.updateGrowthConfig(dto);
  }
}
