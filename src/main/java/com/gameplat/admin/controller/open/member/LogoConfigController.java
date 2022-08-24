package com.gameplat.admin.controller.open.member;

import com.gameplat.admin.model.dto.GrowthLevelLogoEditDTO;
import com.gameplat.admin.model.vo.LogoConfigVO;
import com.gameplat.admin.service.MemberGrowthLevelService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
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

import java.util.List;

/**
 * Logo配置
 *
 * @author lily
 */
@Slf4j
@Tag(name = "Logo配置")
@RestController
@RequestMapping("/api/admin/member/logo")
public class LogoConfigController {

  @Autowired private MemberGrowthLevelService memberGrowthLevelService;

  @Operation(summary = "logo配置列表")
  @GetMapping("/list")
  @PreAuthorize("hasAuthority('member:logo:view')")
  public List<LogoConfigVO> list() {
    return memberGrowthLevelService.getLogoConfig();
  }

  @Operation(summary = "修改logo配置")
  @PostMapping("/edit")
  @PreAuthorize("hasAuthority('member:logo:edit')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'Logo配置-->修改logo配置' + #dto" )
  public void edit(@Validated GrowthLevelLogoEditDTO dto) {
    memberGrowthLevelService.updateLogo(dto);
  }
}
