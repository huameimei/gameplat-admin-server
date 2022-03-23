package com.gameplat.admin.controller.open.member;

import com.gameplat.admin.model.dto.MemberGrowthConfigEditDto;
import com.gameplat.admin.model.vo.MemberGrowthConfigVO;
import com.gameplat.admin.service.MemberGrowthConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lily
 * @description
 * @date 2022/1/15
 */
@Api(tags = "VIP说明配置")
@Slf4j
@RestController
@RequestMapping("/api/admin/member/growthConfig")
public class MemberGrowthConfigController {

  @Autowired private MemberGrowthConfigService memberGrowthConfigService;

  @ApiOperation(value = "查询")
  @GetMapping("/get")
  @PreAuthorize("hasAuthority('member:growthConfig:get')")
  public MemberGrowthConfigVO getOne() {
    return memberGrowthConfigService.findOneConfig();
  }

  @ApiOperation(value = "修改")
  @PutMapping("/edit")
  @PreAuthorize("hasAuthority('member:growthConfig:edit')")
  @ApiParam(name = "修改VIP配置入参", value = "传入json格式", required = true)
  public void update(@Validated MemberGrowthConfigEditDto dto) {
    memberGrowthConfigService.updateGrowthConfig(dto);
  }
}
