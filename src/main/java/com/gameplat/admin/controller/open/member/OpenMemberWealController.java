package com.gameplat.admin.controller.open.member;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.*;
import com.gameplat.admin.model.vo.MemberWealDetailVO;
import com.gameplat.admin.model.vo.MemberWealVO;
import com.gameplat.admin.service.MemberWealDetailService;
import com.gameplat.admin.service.MemberWealService;
import com.gameplat.common.lang.Assert;
import com.gameplat.model.entity.member.MemberWeal;
import com.gameplat.model.entity.member.MemberWealDetail;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lily
 * @description vip福利发放
 * @date 2021/11/22
 */
@Api(tags = "vip福利发放")
@Slf4j
@RestController
@RequestMapping("/api/admin/member/weal")
public class OpenMemberWealController {

  @Autowired private MemberWealService wealService;

  @Autowired private MemberWealDetailService memberWealDetailService;

  @GetMapping("/list")
  @ApiOperation(value = "查询福利发放记录列表")
  @PreAuthorize("hasAuthority('member:weal:view')")
  public IPage<MemberWealVO> listWeal(PageDTO<MemberWeal> page, MemberWealDTO dto) {
    return wealService.findMemberWealList(page, dto);
  }

  @PostMapping("/add")
  @ApiOperation(value = "新增福利发放")
  @PreAuthorize("hasAuthority('member:weal:add')")
  public void addMemberWeal(@RequestBody MemberWealAddDTO dto) {
    wealService.addMemberWeal(dto);
  }

  @PutMapping("/edit")
  @ApiOperation(value = "修改福利发放")
  @PreAuthorize("hasAuthority('member:weal:edit')")
  public void updateWeal(@RequestBody MemberWealEditDTO dto) {
    wealService.updateMemberWeal(dto);
  }

  @ApiOperation(value = "删除福利发放")
  @DeleteMapping("/remove")
  @PreAuthorize("hasAuthority('member:weal:remove')")
  public void removeWeal(Long id) {
    wealService.deleteMemberWeal(id);
  }

  @PutMapping("/settle")
  @ApiOperation(value = "福利结算")
  @PreAuthorize("hasAuthority('member:weal:settle')")
  public void settleWeal(@RequestBody MemberWealSettleDTO settleDTO) {
    Assert.notNull(settleDTO.getId(), "id不能为空");
    wealService.settleWeal(settleDTO.getId());
  }

  @PostMapping("/distribute")
  @ApiOperation(value = "福利派发")
  @PreAuthorize("hasAuthority('member:weal:distribute')")
  public void distributeSalary(@RequestBody MemberWeal dto, HttpServletRequest request) {
    Assert.notNull(dto.getId(), "id不能为空");
    wealService.distributeWeal(dto.getId(), request);
  }

  @PostMapping("/recycle")
  @ApiOperation(value = "福利回收")
  @PreAuthorize("hasAuthority('member:weal:recycle')")
  public void recycleSalary(@RequestBody MemberWeal dto, HttpServletRequest request) {
    Assert.notNull(dto.getId(), "id不能为空");
    wealService.recycleWeal(dto.getId(), request);
  }

  @GetMapping("/details")
  @ApiOperation(value = "详情")
  @PreAuthorize("hasAuthority('member:weal:details')")
  public IPage<MemberWealDetailVO> getDetails(
      PageDTO<MemberWealDetail> page, MemberWealDetailDTO dto) {
    Assert.notNull(dto.getWealId(), "福利id不能为空");
    return memberWealDetailService.findWealDetailList(page, dto);
  }

  @DeleteMapping("/delete")
  @ApiOperation(value = "删除会员俸禄")
  @PreAuthorize("hasAuthority('member:weal:removeMember')")
  public void deleteByUserId(Long id) {
    memberWealDetailService.deleteById(id);
  }

  @PutMapping("/updateRewordAmount")
  @ApiOperation(value = "修改会员俸禄")
  @PreAuthorize("hasAuthority('member:weal:editMember')")
  public void editRewordAmount(@RequestBody MemberWealDetailEditDTO dto) {
    memberWealDetailService.editRewordAmount(dto.getId(), dto.getRewordAmount());
  }
}
