package com.gameplat.admin.controller.open.member;

import com.gameplat.admin.model.dto.MemberRemarkAddDTO;
import com.gameplat.admin.model.vo.MemberRemarkVO;
import com.gameplat.admin.service.MemberRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/member/remark")
public class MemberRemarkController {

  @Autowired private MemberRemarkService memberRemarkService;

  @PostMapping("/update")
  public void update(@RequestBody MemberRemarkAddDTO dto) {
    memberRemarkService.update(dto);
  }

  @GetMapping("/getByMemberId/{memberId}")
  public List<MemberRemarkVO> getByMemberId(@PathVariable Long memberId) {
    return memberRemarkService.getByMemberId(memberId);
  }
}
