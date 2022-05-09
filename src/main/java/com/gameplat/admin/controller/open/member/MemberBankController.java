package com.gameplat.admin.controller.open.member;

import com.gameplat.admin.model.dto.MemberBankAddDTO;
import com.gameplat.admin.model.dto.MemberBankEditDTO;
import com.gameplat.admin.model.vo.MemberBankVO;
import com.gameplat.admin.service.MemberBankService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "会员银行")
@RestController
@RequestMapping("/api/admin/member/bank/")
public class MemberBankController {

  @Autowired private MemberBankService memberBankService;

  @ApiOperation("查询")
  @GetMapping("/list/{memberId}/{type}")
  @PreAuthorize("hasAuthority('member:bank:view')")
  public List<MemberBankVO> list(@PathVariable Long memberId, @PathVariable String type) {
    return memberBankService.getMemberBankList(memberId, type);
  }

  @ApiOperation("添加")
  @PostMapping("/add")
  @PreAuthorize("hasAuthority('member:bank:add')")
  public void add(@Validated @RequestBody MemberBankAddDTO dto) {
    memberBankService.add(dto);
  }

  @ApiOperation("编辑")
  @PutMapping("/edit")
  @PreAuthorize("hasAuthority('member:bank:edit')")
  public void edit(@Validated @RequestBody MemberBankEditDTO dto) {
    memberBankService.edit(dto);
  }

  @ApiOperation("删除")
  @DeleteMapping("/remove/{id}")
  @PreAuthorize("hasAuthority('member:bank:remove')")
  public void remove(@PathVariable Long id) {
    memberBankService.removeById(id);
  }

  @ApiOperation("设置默认")
  @PutMapping("/setDefault/{id}")
  @PreAuthorize("hasAuthority('member:bank:edit')")
  public void setDefault(@PathVariable Long id) {
    memberBankService.setDefault(id);
  }
}
