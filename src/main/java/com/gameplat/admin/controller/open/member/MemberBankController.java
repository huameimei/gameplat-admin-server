package com.gameplat.admin.controller.open.member;

import com.gameplat.admin.model.dto.MemberBankAddDTO;
import com.gameplat.admin.model.dto.MemberBankEditDTO;
import com.gameplat.admin.model.vo.MemberBankVO;
import com.gameplat.admin.service.MemberBankService;
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

@Tag(name = "会员银行")
@RestController
@RequestMapping("/api/admin/member/bank/")
public class MemberBankController {

  @Autowired private MemberBankService memberBankService;

  @Operation(summary = "查询")
  @GetMapping("/list/{memberId}/{type}")
  @PreAuthorize("hasAuthority('member:bank:view')")
  public List<MemberBankVO> list(@PathVariable Long memberId, @PathVariable String type) {
    return memberBankService.getMemberBankList(memberId, type);
  }

  @Operation(summary = "添加")
  @PostMapping("/add")
  @PreAuthorize("hasAuthority('member:bank:add')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'会员银行-->添加' + #dto" )
  public void add(@Validated @RequestBody MemberBankAddDTO dto) {
    memberBankService.add(dto);
  }

  @Operation(summary = "编辑")
  @PostMapping("/edit")
  @PreAuthorize("hasAuthority('member:bank:edit')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'会员银行-->编辑' + #dto" )
  public void edit(@Validated @RequestBody MemberBankEditDTO dto) {
    memberBankService.edit(dto);
  }

  @Operation(summary = "删除")
  @PostMapping("/remove/{id}")
  @PreAuthorize("hasAuthority('member:bank:remove')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'会员银行-->删除' + #id" )
  public void remove(@PathVariable Long id) {
    memberBankService.removeById(id);
  }

  @Operation(summary = "设置默认")
  @PostMapping("/setDefault/{id}")
  @PreAuthorize("hasAuthority('member:bank:edit')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'会员银行-->设置默认' + #id" )
  public void setDefault(@PathVariable Long id) {
    memberBankService.setDefault(id);
  }
}
