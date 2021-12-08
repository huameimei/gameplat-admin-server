package com.gameplat.admin.controller.open.member;

import com.gameplat.admin.model.dto.MemberBankAddDTO;
import com.gameplat.admin.model.dto.MemberBankEditDTO;
import com.gameplat.admin.model.vo.MemberBankVO;
import com.gameplat.admin.service.MemberBankService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/member/bank/")
public class MemberBankController {

  @Autowired private MemberBankService memberBankService;

  @GetMapping("/list/{memberId}/{type}")
  public List<MemberBankVO> list(@PathVariable Long memberId, @PathVariable String type) {
    return memberBankService.getMemberBankList(memberId, type);
  }

  @PostMapping("/add")
  public void add(@Validated @RequestBody MemberBankAddDTO dto) {
    memberBankService.add(dto);
  }

  @PutMapping("/edit")
  public void edit(@Validated @RequestBody MemberBankEditDTO dto) {
    memberBankService.edit(dto);
  }

  @DeleteMapping("/remove/{id}")
  public void remove(@PathVariable Long id) {
    memberBankService.removeById(id);
  }

  @PutMapping("/setDefault/{id}")
  public void setDefault(@PathVariable Long id) {
    memberBankService.setDefault(id);
  }
}
