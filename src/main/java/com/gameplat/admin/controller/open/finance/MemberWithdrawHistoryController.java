package com.gameplat.admin.controller.open.finance;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.dto.MemberWithdrawHistoryQueryDTO;
import com.gameplat.admin.model.vo.MemberWithdrawHistorySummaryVO;
import com.gameplat.admin.model.vo.MemberWithdrawHistoryVO;
import com.gameplat.admin.service.MemberWithdrawHistoryService;
import com.gameplat.model.entity.member.MemberWithdrawHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/finance/memberWithdrawHistory")
public class MemberWithdrawHistoryController {

  @Autowired private MemberWithdrawHistoryService userWithdrawHistoryService;

  @PostMapping("/page")
  @PreAuthorize("hasAuthority('finance:memberWithdrawHistory:page')")
  public IPage<MemberWithdrawHistoryVO> queryPage(
      Page<MemberWithdrawHistory> page, MemberWithdrawHistoryQueryDTO dto) {
    return userWithdrawHistoryService.findPage(page, dto);
  }

  @PostMapping("/findSumMemberWithdrawHistory")
  @PreAuthorize("hasAuthority('finance:memberWithdrawHistory:findSumMemberWithdrawHistory')")
  public MemberWithdrawHistorySummaryVO findSumMemberWithdrawHistory(
      MemberWithdrawHistoryQueryDTO dto) {
    return userWithdrawHistoryService.findSumMemberWithdrawHistory(dto);
  }
}
