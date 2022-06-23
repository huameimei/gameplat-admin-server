package com.gameplat.admin.controller.open.finance;

import com.gameplat.admin.model.bean.PayLeaderboardParam;
import com.gameplat.admin.model.bean.PayLeaderboardResult;
import com.gameplat.admin.model.bean.PayLeaderboardSearch;
import com.gameplat.admin.service.PayLeaderboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "支付排行榜")
@RestController
@RequestMapping("/api/admin/finance/rank")
public class PayLeaderboardController {

  @Autowired private PayLeaderboardService payLeaderboardService;

  @Operation(summary = "获取支付排行榜")
  @PostMapping("/getLeaderboard")
  @PreAuthorize("hasAuthority('thirdParty:leaderboard:view')")
  public PayLeaderboardResult getLeaderboard(@RequestBody PayLeaderboardParam payLeaderboardParam) {
    return payLeaderboardService.getLeaderboard(payLeaderboardParam);
  }

  @Operation(summary = "获取支付排行榜搜索参数")
  @GetMapping("/getLeaderboardSearch")
  @PreAuthorize("hasAuthority('thirdParty:leaderboard:view')")
  public PayLeaderboardSearch getLeaderboardSearch() {
    return payLeaderboardService.getLeaderboardSearch();
  }
}
