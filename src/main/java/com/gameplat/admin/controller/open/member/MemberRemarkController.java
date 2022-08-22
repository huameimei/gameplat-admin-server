package com.gameplat.admin.controller.open.member;

import com.gameplat.admin.model.dto.MemberRemarkAddDTO;
import com.gameplat.admin.model.vo.MemberRemarkVO;
import com.gameplat.admin.service.MemberRemarkService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "会员备注")
@RestController
@RequestMapping("/api/admin/member/remark")
public class MemberRemarkController {

  @Autowired private MemberRemarkService memberRemarkService;

  @Operation(summary = "获取会员备注")
  @GetMapping("/getByMemberId/{memberId}")
  @PreAuthorize("hasAuthority('member:remark:view')")
  public List<MemberRemarkVO> getByMemberId(@PathVariable Long memberId) {
    return memberRemarkService.getByMemberId(memberId);
  }

  @Operation(summary = "添加会员备注")
  @PostMapping("/batchAdd")
  @PreAuthorize("hasAuthority('member:remark:batchAdd')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'会员备注-->添加会员备注:' + #dto" )
  public void batchAdd(@Validated @RequestBody MemberRemarkAddDTO dto) {
    memberRemarkService.batchAdd(dto);
  }

  @Operation(summary = "删除会员备注")
  @PostMapping("/delete/{id}")
  @PreAuthorize("hasAuthority('member:remark:remove')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'会员备注-->删除会员备注:' + #id" )
  public void deleteByRemarkId(@PathVariable Long id) {
    memberRemarkService.deleteById(id);
  }

  @Operation(summary = "清空会员备注")
  @PostMapping("/clean/{memberId}")
  @PreAuthorize("hasAuthority('member:remark:clean')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'会员备注-->清空会员备注:' + #memberId" )
  public void cleanByMemberId(@PathVariable Long memberId) {
    memberRemarkService.cleanByMemberId(memberId);
  }
}
