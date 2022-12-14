package com.gameplat.admin.controller.open.member;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.MemberGrowthStatisDTO;
import com.gameplat.admin.model.vo.MemberGrowthStatisVO;
import com.gameplat.admin.service.MemberGrowthStatisService;
import com.gameplat.model.entity.member.MemberGrowthStatis;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * VIP成长值汇总数据
 *
 * @author lily
 */
@Tag(name = "VIP成长值汇总数据")
@Slf4j
@RestController
@RequestMapping("/api/admin/member/growthStatis")
public class MemberGrowthStatisController {

  @Autowired private MemberGrowthStatisService memberGrowthStatisService;

  @Operation(summary = "查询")
  @GetMapping("/page")
  @PreAuthorize("hasAuthority('member:growthStatis:view')")
  public IPage<MemberGrowthStatisVO> page(
      PageDTO<MemberGrowthStatis> page, MemberGrowthStatisDTO dto) {
    return memberGrowthStatisService.findStatisList(page, dto);
  }
}
