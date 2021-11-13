package com.gameplat.admin.controller.open.finance;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.domain.MemberWithdrawHistory;
import com.gameplat.admin.model.dto.UserWithdrawHistoryQueryDTO;
import com.gameplat.admin.model.vo.MemberWithdrawHistoryVO;
import com.gameplat.admin.service.MemberWithdrawHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/finance/memberWithdrawHistory")
public class MemberWithdrawHistoryController {

  @Autowired
  private MemberWithdrawHistoryService userWithdrawHistoryService;

  @PostMapping("/page")
  @PreAuthorize("hasAuthority('finance:memberWithdrawHistory:page')")
  public IPage<MemberWithdrawHistoryVO> queryPage(Page<MemberWithdrawHistory> page, UserWithdrawHistoryQueryDTO dto) {
    return userWithdrawHistoryService.findPage(page, dto);
  }

}
