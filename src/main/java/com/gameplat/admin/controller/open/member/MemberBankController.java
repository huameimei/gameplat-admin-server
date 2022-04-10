package com.gameplat.admin.controller.open.member;

import com.gameplat.admin.model.dto.MemberBankAddDTO;
import com.gameplat.admin.model.dto.MemberBankEditDTO;
import com.gameplat.admin.model.vo.MemberBankVO;
import com.gameplat.admin.service.MemberBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/member/bank/")
public class MemberBankController {

  @Autowired private MemberBankService memberBankService;

  @GetMapping("/list/{memberId}/{type}")
  @PreAuthorize("hasAuthority('member:bank:view')")
  public List<MemberBankVO> list(@PathVariable Long memberId, @PathVariable String type) {
    return memberBankService.getMemberBankList(memberId, type);
  }

  @PostMapping("/add")
  @PreAuthorize("hasAuthority('member:bank:add')")
  public void add(@Validated @RequestBody MemberBankAddDTO dto) {
    memberBankService.add(dto);
  }

  @PutMapping("/edit")
  @PreAuthorize("hasAuthority('member:bank:edit')")
  public void edit(@Validated @RequestBody MemberBankEditDTO dto) {
    memberBankService.edit(dto);
  }

  @DeleteMapping("/remove/{id}")
  @PreAuthorize("hasAuthority('member:bank:remove')")
  public void remove(@PathVariable Long id) {
    memberBankService.removeById(id);
  }

  @PutMapping("/setDefault/{id}")
  @PreAuthorize("hasAuthority('member:bank:edit')")
  public void setDefault(@PathVariable Long id) {
    memberBankService.setDefault(id);
  }
}
