package com.gameplat.admin.controller.open.member;

import com.gameplat.admin.model.dto.MemberRemarkAddDTO;
import com.gameplat.admin.model.vo.MemberRemarkVO;
import com.gameplat.admin.service.MemberRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/member/remark")
public class MemberRemarkController {

  @Autowired private MemberRemarkService memberRemarkService;

  @GetMapping("/getByMemberId/{memberId}")
  @PreAuthorize("hasAuthority('member:remark:view')")
  public List<MemberRemarkVO> getByMemberId(@PathVariable Long memberId) {
    return memberRemarkService.getByMemberId(memberId);
  }

  @PostMapping("/batchAdd")
  @PreAuthorize("hasAuthority('member:remark:batchAdd')")
  public void batchAdd(@Validated @RequestBody MemberRemarkAddDTO dto) {
    memberRemarkService.batchAdd(dto);
  }

  @DeleteMapping("/delete/{id}")
  @PreAuthorize("hasAuthority('member:remark:remove')")
  public void deleteByRemarkId(@PathVariable Long id) {
    memberRemarkService.deleteById(id);
  }

  @DeleteMapping("/clean/{memberId}")
  @PreAuthorize("hasAuthority('member:remark:clean')")
  public void cleanByMemberId(@PathVariable Long memberId) {
    memberRemarkService.cleanByMemberId(memberId);
  }
}
