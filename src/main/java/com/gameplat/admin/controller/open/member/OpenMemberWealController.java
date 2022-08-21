package com.gameplat.admin.controller.open.member;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.*;
import com.gameplat.admin.model.vo.MemberWealDetailVO;
import com.gameplat.admin.model.vo.MemberWealVO;
import com.gameplat.admin.service.MemberWealDetailService;
import com.gameplat.admin.service.MemberWealService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.common.lang.Assert;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.member.MemberWeal;
import com.gameplat.model.entity.member.MemberWealDetail;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * vip福利发放
 *
 * @author lily
 */
@Tag(name = "vip福利发放")
@Slf4j
@RestController
@RequestMapping("/api/admin/member/weal")
public class OpenMemberWealController {

  @Autowired private MemberWealService wealService;

  @Autowired private MemberWealDetailService memberWealDetailService;

  @GetMapping("/list")
  @Operation(summary = "查询福利发放记录列表")
  @PreAuthorize("hasAuthority('member:weal:view')")
  public IPage<MemberWealVO> listWeal(PageDTO<MemberWeal> page, MemberWealDTO dto) {
    return wealService.findMemberWealList(page, dto);
  }

  @PostMapping("/add")
  @Operation(summary = "新增福利发放")
  @PreAuthorize("hasAuthority('member:weal:add')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'vip福利发放-->新增福利发放:' + #dto" )
  public void addMemberWeal(@RequestBody MemberWealAddDTO dto) {
    wealService.addMemberWeal(dto);
  }

  @PostMapping("/edit")
  @Operation(summary = "修改福利发放")
  @PreAuthorize("hasAuthority('member:weal:edit')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'vip福利发放-->修改福利发放:' + #dto" )
  public void updateWeal(@RequestBody MemberWealEditDTO dto) {
    wealService.updateMemberWeal(dto);
  }

  @Operation(summary = "删除福利发放")
  @PostMapping("/remove")
  @PreAuthorize("hasAuthority('member:weal:remove')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'vip福利发放-->删除福利发放:' + #id" )
  public void removeWeal(Long id) {
    wealService.deleteMemberWeal(id);
  }

  @PostMapping("/settle")
  @Operation(summary = "福利结算")
  @PreAuthorize("hasAuthority('member:weal:settle')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'vip福利发放-->福利结算:' + #settleDTO")
  public void settleWeal(@RequestBody MemberWealSettleDTO settleDTO) {
    Assert.notNull(settleDTO.getId(), "id不能为空");
    wealService.settleWeal(settleDTO.getId());
  }

  @PostMapping("/distribute")
  @Operation(summary = "福利派发")
  @PreAuthorize("hasAuthority('member:weal:distribute')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'vip福利发放-->福利派发:' + #dto")
  public void distributeSalary(@RequestBody MemberWeal dto, HttpServletRequest request) {
    Assert.notNull(dto.getId(), "id不能为空");
    wealService.distributeWeal(dto.getId(), request);
  }

  @PostMapping("/recycle")
  @Operation(summary = "福利回收")
  @PreAuthorize("hasAuthority('member:weal:recycle')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'vip福利发放-->福利回收:' + #dto")
  public void recycleSalary(@RequestBody MemberWeal dto, HttpServletRequest request) {
    Assert.notNull(dto.getId(), "id不能为空");
    wealService.recycleWeal(dto.getId(), request);
  }

  @GetMapping("/details")
  @Operation(summary = "详情")
  @PreAuthorize("hasAuthority('member:weal:details')")
  public IPage<MemberWealDetailVO> getDetails(
      PageDTO<MemberWealDetail> page, MemberWealDetailDTO dto) {
    Assert.notNull(dto.getWealId(), "福利id不能为空");
    return memberWealDetailService.findWealDetailList(page, dto);
  }

  @PostMapping("/delete")
  @Operation(summary = "删除会员俸禄")
  @PreAuthorize("hasAuthority('member:weal:removeMember')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'vip福利发放-->删除会员俸禄:' + #id")
  public void deleteByUserId(Long id) {
    memberWealDetailService.deleteById(id);
  }

  @PostMapping("/updateRewordAmount")
  @Operation(summary = "修改会员俸禄")
  @PreAuthorize("hasAuthority('member:weal:editMember')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'vip福利发放-->修改会员俸禄:' + #dto")
  public void editRewordAmount(@RequestBody MemberWealDetailEditDTO dto) {
    memberWealDetailService.editRewordAmount(dto.getId(), dto.getRewordAmount());
  }
}
