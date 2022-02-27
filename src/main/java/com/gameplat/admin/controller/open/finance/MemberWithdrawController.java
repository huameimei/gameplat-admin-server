package com.gameplat.admin.controller.open.finance;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.bean.PageExt;
import com.gameplat.admin.model.domain.MemberWithdraw;
import com.gameplat.admin.model.domain.PpMerchant;
import com.gameplat.admin.model.dto.MemberWithdrawQueryDTO;
import com.gameplat.admin.model.vo.MemberWithdrawVO;
import com.gameplat.admin.model.vo.SummaryVO;
import com.gameplat.admin.service.MemberWithdrawService;
import com.gameplat.common.model.bean.UserEquipment;
import com.gameplat.security.SecurityUserHolder;
import com.gameplat.security.context.UserCredential;
import java.math.BigDecimal;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/finance/memberWithdraw")
public class MemberWithdrawController {

  @Autowired
  private MemberWithdrawService userWithdrawService;

  @PostMapping("/modifyCashStatus")
  @PreAuthorize("hasAuthority('finance:memberWithdraw:modifyCashStatus')")
  public void modifyCashStatus(Long id, Integer cashStatus, Integer curStatus, boolean isDirect,
      String approveReason,HttpServletRequest request) throws Exception{
    UserCredential userCredential = SecurityUserHolder.getCredential();
    UserEquipment clientInfo = UserEquipment.create(request);
    userWithdrawService.modify(id, cashStatus, curStatus, isDirect, approveReason, userCredential,clientInfo);
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
  public PageExt<MemberWithdrawVO, SummaryVO> queryPage(Page<MemberWithdraw> page, MemberWithdrawQueryDTO dto) {
    return userWithdrawService.findPage(page, dto);
  }

  @PostMapping("/queryAvailableMerchant")
  @PreAuthorize("hasAuthority('finance:memberWithdraw:queryAvailableMerchant')")
  public List<PpMerchant> queryAvailableMerchant(Long id) {
    return userWithdrawService.queryProxyMerchant(id);
  }

  @PostMapping("/save")
  @PreAuthorize("hasAuthority('finance:memberWithdraw:save')")
  public void save(BigDecimal cashMoney, String cashReason, Integer handPoints,Long memberId)
      throws Exception {
    UserCredential userCredential = SecurityUserHolder.getCredential();
    userWithdrawService.save(cashMoney, cashReason, handPoints, userCredential, memberId);
  }

}
