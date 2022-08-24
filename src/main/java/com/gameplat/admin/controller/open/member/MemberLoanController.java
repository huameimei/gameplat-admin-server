package com.gameplat.admin.controller.open.member;

import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.MemberLoanQueryDTO;
import com.gameplat.admin.model.vo.LoanVO;
import com.gameplat.admin.service.MemberLoanService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.member.MemberLoan;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 借呗相关
 *
 * @author lily
 */
@Tag(name = "借呗")
@Slf4j
@RestController
@RequestMapping("/api/admin/member/loan")
public class MemberLoanController {

  @Autowired private MemberLoanService memberLoanService;

  @GetMapping("/page")
  @Operation(summary = "获取借呗记录")
  @PreAuthorize("hasAuthority('member:loan:view')")
  public LoanVO page(PageDTO<MemberLoan> page, MemberLoanQueryDTO dto) {
    return memberLoanService.page(page, dto);
  }

  @PostMapping("/recycle")
  @Operation(summary = "欠款回收")
  @PreAuthorize("hasAuthority('member:loan:recycle')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'借呗-->欠款回收:' + #idList" )
  public void recycle(String idList) {
    memberLoanService.recycle(idList);
  }
}
