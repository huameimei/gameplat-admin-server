package com.gameplat.admin.controller.open.member;

import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.MemberLoanQueryDTO;
import com.gameplat.admin.model.vo.LoanVO;
import com.gameplat.admin.service.MemberLoanService;
import com.gameplat.model.entity.member.MemberLoan;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 借呗相关
 *
 * @author lily
 */
@Api(tags = "借呗")
@Slf4j
@RestController
@RequestMapping("/api/admin/member/loan")
public class MemberLoanController {

  @Autowired private MemberLoanService memberLoanService;

  @GetMapping("/page")
  @ApiOperation(value = "获取借呗记录")
  @PreAuthorize("hasAuthority('member:loan:view')")
  public LoanVO page(PageDTO<MemberLoan> page, MemberLoanQueryDTO dto) {
    return memberLoanService.page(page, dto);
  }

  @PostMapping("/recycle")
  @ApiOperation(value = "欠款回收")
  @PreAuthorize("hasAuthority('member:loan:recycle')")
  public void recycle(String idList) {
    memberLoanService.recycle(idList);
  }
}
