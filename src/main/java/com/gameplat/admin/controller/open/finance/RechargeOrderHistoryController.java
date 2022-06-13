package com.gameplat.admin.controller.open.finance;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.dto.RechargeOrderHistoryQueryDTO;
import com.gameplat.admin.model.vo.MemberRechBalanceVO;
import com.gameplat.admin.model.vo.RechargeHistorySummaryVO;
import com.gameplat.admin.model.vo.RechargeOrderHistoryVO;
import com.gameplat.admin.service.RechargeOrderHistoryService;
import com.gameplat.base.common.util.EasyExcelUtil;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.common.model.bean.UserEquipment;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.recharge.RechargeOrderHistory;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/api/admin/finance/rechargeOrderHistory")
@Log4j2
public class RechargeOrderHistoryController {

  @Autowired private RechargeOrderHistoryService rechargeOrderHistoryService;

  @PostMapping("/page")
  @PreAuthorize("hasAuthority('finance:rechargeOrderHistory:view')")
  public IPage<RechargeOrderHistoryVO> queryPage(
      Page<RechargeOrderHistory> page, RechargeOrderHistoryQueryDTO dto) {
    return rechargeOrderHistoryService.findPage(page, dto);
  }

  @PostMapping("/statRechargeHisTotal")
  @PreAuthorize("hasAuthority('finance:rechargeOrderHistory:statRechargeHisTotal')")
  public RechargeHistorySummaryVO statRechargeHisTotal(RechargeOrderHistoryQueryDTO dto) {
    return rechargeOrderHistoryService.findSumRechargeOrderHistory(dto);
  }

  @PostMapping("/statRechargeHisTotalYesterday")
  @PreAuthorize("hasAuthority('finance:rechargeOrderHistory:statRechargeHisTotalYesterday')")
  public RechargeHistorySummaryVO statRechargeHisTotalYesterday(RechargeOrderHistoryQueryDTO dto) {
    return rechargeOrderHistoryService.findSumRechargeOrderHistory(dto);
  }


  @Operation(summary = "充值导出")
  @PostMapping("/rechReport")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.RECHARGE, desc = "'人工入款文件批量充值")
  @PreAuthorize("hasAuthority('finance:rechargeOrder:rechReport')")
  public void fileRech(
          RechargeOrderHistoryQueryDTO dto, HttpServletRequest request, HttpServletResponse response) {
    log.info("批量上传数据：{}", JSONUtil.toJsonStr(dto));
    rechargeOrderHistoryService.rechReport(dto, request, response);
  }

}
