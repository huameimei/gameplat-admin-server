package com.gameplat.admin.controller.open.member;

import com.gameplat.admin.model.dto.MemberRemarkAddDTO;
import com.gameplat.admin.model.vo.MemberRemarkVO;
import com.gameplat.admin.service.MemberRemarkService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/member/remark")
public class MemberRemarkController {

  @Autowired private MemberRemarkService memberRemarkService;

  @GetMapping("/getByMemberId/{memberId}")
  @PreAuthorize("hasAuthority('member:remark:getByMemberId')")
  public List<MemberRemarkVO> getByMemberId(@PathVariable Long memberId) {
    return memberRemarkService.getByMemberId(memberId);
  }

  @PostMapping("/add")
  @PreAuthorize("hasAuthority('member:remark:add')")
  public void add(@RequestBody MemberRemarkAddDTO addDTO) {
    memberRemarkService.update(addDTO.getId(), addDTO.getRemark());
  }

  @PostMapping("/batchAdd")
  @PreAuthorize("hasAuthority('member:remark:batchAdd')")
  public void batchAdd(@RequestBody MemberRemarkAddDTO addDTO) {
    memberRemarkService.batchAdd(addDTO);
  }

  @DeleteMapping("/delete/{id}")
  @PreAuthorize("hasAuthority('member:remark:delete')")
  public void deleteByRemarkId(@PathVariable Long id) {
    memberRemarkService.deleteById(id);
  }

  @DeleteMapping("/clean/{memberId}")
  @PreAuthorize("hasAuthority('member:remark:clean')")
  public void cleanByMemberId(@PathVariable Long memberId) {
    memberRemarkService.cleanByMemberId(memberId);
  }


}
