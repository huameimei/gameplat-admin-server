package com.gameplat.admin.controller.open.member;

import com.gameplat.admin.model.dto.GrowthLevelLogoEditDTO;
import com.gameplat.admin.model.vo.LogoConfigVO;
import com.gameplat.admin.service.MemberGrowthLevelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Logo配置
 *
 * @author lily
 */
@Slf4j
@Api(tags = "Logo配置")
@RestController
@RequestMapping("/api/admin/member/logo")
public class LogoConfigController {

  @Autowired private MemberGrowthLevelService memberGrowthLevelService;

  @ApiOperation("logo配置列表")
  @GetMapping("/list")
  @PreAuthorize("hasAuthority('member:logo:view')")
  public List<LogoConfigVO> list() {
    return memberGrowthLevelService.getLogoConfig();
  }

  @ApiOperation("修改logo配置")
  @PostMapping("/edit")
  @PreAuthorize("hasAuthority('member:logo:edit')")
  public void edit(@Validated GrowthLevelLogoEditDTO dto) {
    memberGrowthLevelService.updateLogo(dto);
  }
}
