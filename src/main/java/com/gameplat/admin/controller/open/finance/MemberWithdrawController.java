package com.gameplat.admin.controller.open.finance;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.bean.PageExt;
import com.gameplat.admin.model.dto.MemberWithdrawDTO;
import com.gameplat.admin.model.dto.MemberWithdrawQueryDTO;
import com.gameplat.admin.model.vo.MemberWithdrawVO;
import com.gameplat.admin.model.vo.SummaryVO;
import com.gameplat.admin.service.MemberWithdrawService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.common.enums.WithdrawStatus;
import com.gameplat.common.model.bean.UserEquipment;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.member.MemberWithdraw;
import com.gameplat.model.entity.pay.PpMerchant;
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
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.WITHDRAW,
      desc = "'修改提款订单状态为:' + #cashStatus")
  public void modifyCashStatus(
      Long id,
      Integer cashStatus,
      Integer curStatus,
      boolean isDirect,
      HttpServletRequest request)
      throws Exception {
    UserCredential userCredential = SecurityUserHolder.getCredential();
    UserEquipment clientInfo = UserEquipment.create(request);
    userWithdrawService.modify(
        id, cashStatus, curStatus, isDirect, userCredential, clientInfo);
  }

  @PostMapping("/batchHandle")
  @PreAuthorize("hasAuthority('finance:memberWithdraw:batchHandle')")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.WITHDRAW,
      desc = "'批量受理订单:' + #list")
  public String batchHandle(String list,
      HttpServletRequest request)
      throws Exception {
    if (null == list) {
      throw new ServiceException("批量受理请求参数为空");
    }
    UserCredential userCredential = SecurityUserHolder.getCredential();
    UserEquipment clientInfo = UserEquipment.create(request);
    List<MemberWithdrawDTO> memberWithdrawDTOList = JSONUtil.toList(list, MemberWithdrawDTO.class);
    for (MemberWithdrawDTO memberWithdrawDTO : memberWithdrawDTOList) {
      userWithdrawService.modify(
          memberWithdrawDTO.getId(), WithdrawStatus.HANDLED.getValue(),
          memberWithdrawDTO.getCurStatus(), memberWithdrawDTO.getIsDirect(), userCredential,
          clientInfo);
    }
    return "成功受理" + memberWithdrawDTOList.size() + "条订单";
  }

  @PostMapping("/batchUnHandle")
  @PreAuthorize("hasAuthority('finance:memberWithdraw:batchUnHandle')")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.WITHDRAW,
      desc = "'批量取消受理订单:' + #list")
  public String batchModifyCashStatus(String list,
      HttpServletRequest request)
      throws Exception {
    if (null == list) {
      throw new ServiceException("批量取消受理请求参数为空");
    }
    UserCredential userCredential = SecurityUserHolder.getCredential();
    UserEquipment clientInfo = UserEquipment.create(request);
    List<MemberWithdrawDTO> memberWithdrawDTOList = JSONUtil.toList(list, MemberWithdrawDTO.class);
    for (MemberWithdrawDTO memberWithdrawDTO : memberWithdrawDTOList) {
      userWithdrawService.modify(
          memberWithdrawDTO.getId(), WithdrawStatus.UNHANDLED.getValue(),
          memberWithdrawDTO.getCurStatus(), memberWithdrawDTO.getIsDirect(), userCredential,
          clientInfo);
    }
    return "成功取消受理" + memberWithdrawDTOList.size() + "条订单";
  }

  @PostMapping("/batchWithdraw")
  @PreAuthorize("hasAuthority('finance:memberWithdraw:batchWithdraw')")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.WITHDRAW,
      desc = "'批量出款订单:' + #list")
  public String batchWithdraw(String list,
      HttpServletRequest request)
      throws Exception {
    if (null == list) {
      throw new ServiceException("批量出款请求参数为空");
    }
    UserCredential userCredential = SecurityUserHolder.getCredential();
    UserEquipment clientInfo = UserEquipment.create(request);
    List<MemberWithdrawDTO> memberWithdrawDTOList = JSONUtil.toList(list, MemberWithdrawDTO.class);
    for (MemberWithdrawDTO memberWithdrawDTO : memberWithdrawDTOList) {
      userWithdrawService.modify(
          memberWithdrawDTO.getId(), WithdrawStatus.SUCCESS.getValue(),
          memberWithdrawDTO.getCurStatus(), memberWithdrawDTO.getIsDirect(), userCredential,
          clientInfo);
    }
    return "成功出款" + memberWithdrawDTOList.size() + "条订单";
  }

  @PostMapping("/editDiscount")
  @PreAuthorize("hasAuthority('finance:memberWithdraw:editDiscount')")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.WITHDRAW,
      desc = "'修改手续费为:' + #afterCounterFee")
  public void updateDiscount(Long id, BigDecimal afterCounterFee) {
    userWithdrawService.updateCounterFee(id, afterCounterFee);
  }

  @PostMapping("/editRemarks")
  @PreAuthorize("hasAuthority('finance:memberWithdraw:editRemarks')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.WITHDRAW, desc = "'修改备注为:' + #remarks")
  public void updateRemarks(Long id, String remarks) {
    userWithdrawService.updateRemarks(id, remarks);
  }

  @PostMapping("/page")
  @PreAuthorize("hasAuthority('finance:memberWithdraw:page')")
  public PageExt<MemberWithdrawVO, SummaryVO> queryPage(
      Page<MemberWithdraw> page, MemberWithdrawQueryDTO dto) {
    return userWithdrawService.findPage(page, dto);
  }

  @PostMapping("/queryAvailableMerchant")
  @PreAuthorize("hasAuthority('finance:memberWithdraw:queryAvailableMerchant')")
  public List<PpMerchant> queryAvailableMerchant(Long id) {
    return userWithdrawService.queryProxyMerchant(id);
  }

  @PostMapping("/save")
  @PreAuthorize("hasAuthority('finance:memberWithdraw:save')")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.WITHDRAW,
      desc = "'人工出款memberId =' + #memberId")
  public void save(BigDecimal cashMoney, String cashReason, Integer handPoints, Long memberId)
      throws Exception {
    UserCredential userCredential = SecurityUserHolder.getCredential();
    userWithdrawService.save(cashMoney, cashReason, handPoints, userCredential, memberId);
  }
}
