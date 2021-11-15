package com.gameplat.admin.controller.open.finance;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.domain.MemberWithdraw;
import com.gameplat.admin.model.dto.UserWithdrawQueryDTO;
import com.gameplat.admin.model.vo.MemberWithdrawVO;
import com.gameplat.admin.model.vo.PpMerchantVO;
import com.gameplat.admin.service.MemberWithdrawService;
import com.gameplat.security.SecurityUserHolder;
import com.gameplat.security.context.UserCredential;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/admin/finance/memberWithdraw")
public class MemberWithdrawController {

  @Autowired private MemberWithdrawService userWithdrawService;

  @PostMapping("/modifyCashStatus")
  @PreAuthorize("hasAuthority('finance:memberWithdraw:modifyCashStatus')")
  public void modifyCashStatus(
      Long id, Integer cashStatus, Integer curStatus, boolean isDirect, String approveReason) {
    UserCredential userCredential = SecurityUserHolder.getCredential();
    userWithdrawService.modify(id, cashStatus, curStatus, isDirect, approveReason, userCredential);
  }

  @PostMapping("/editDiscount")
  @PreAuthorize("hasAuthority('finance:memberWithdraw:editDiscount')")
  public void updateDiscount(Long id, BigDecimal afterCounterFee) {
    userWithdrawService.updateCounterFee(id, afterCounterFee);
  }

  @PostMapping("/editRemarks")
  @PreAuthorize("hasAuthority('finance:memberWithdraw:editRemarks')")
  public void updateRemarks(Long id, String remarks) {
    userWithdrawService.updateRemarks(id, remarks);
  }

  @PostMapping("/page")
  @PreAuthorize("hasAuthority('finance:memberWithdraw:page')")
  public IPage<MemberWithdrawVO> queryPage(Page<MemberWithdraw> page, UserWithdrawQueryDTO dto) {
    return userWithdrawService.findPage(page, dto);
  }

  @PostMapping("/relProxyPay")
  @PreAuthorize("hasAuthority('finance:memberWithdraw:relProxyPay')")
  public void proxyPay(
      @NotNull(message = "{NoNull}") Long id,
      @NotNull(message = "{NoNull}") Long ppMerchantId,
      HttpServletRequest request)
      throws Exception {
    String sysPath = request.getSession().getServletContext().getRealPath("");
    String urL = request.getRequestURL().toString();
    String realUrl = "";
    String scheme = request.getHeader("X-Forwarded-Scheme");
    UserCredential userCredential = SecurityUserHolder.getCredential();
    if (StringUtils.isNotBlank(scheme) && scheme.equals("https")) {
      realUrl = urL.replace("http:", "https:");
    } else {
      realUrl = urL;
    }
    userWithdrawService.proxyPay(id, ppMerchantId, realUrl, sysPath, userCredential);
  }

  @PostMapping("/queryAll")
  @PreAuthorize("hasAuthority('finance:memberWithdraw:queryAll')")
  public List<PpMerchantVO> queryAll(Long id) {
    return userWithdrawService.queryProxyMerchant(id);
  }

  @PostMapping("/save")
  @PreAuthorize("hasAuthority('finance:memberWithdraw:save')")
  public void save(BigDecimal cashMoney, String cashReason, Integer handPoints) throws Exception {
    UserCredential userCredential = SecurityUserHolder.getCredential();
    userWithdrawService.save(cashMoney, cashReason, handPoints, userCredential);
  }
}
