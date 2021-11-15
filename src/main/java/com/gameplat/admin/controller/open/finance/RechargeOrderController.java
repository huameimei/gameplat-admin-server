package com.gameplat.admin.controller.open.finance;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.bean.ManualRechargeOrderBo;
import com.gameplat.admin.model.domain.RechargeOrder;
import com.gameplat.admin.model.dto.RechargeOrderQueryDTO;
import com.gameplat.admin.model.vo.RechargeOrderVO;
import com.gameplat.admin.service.RechargeOrderService;
import com.gameplat.common.exception.ServiceException;
import com.gameplat.security.SecurityUserHolder;
import com.gameplat.security.context.UserCredential;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/admin/finance/rechargeOrder")
public class RechargeOrderController {

  @Autowired private RechargeOrderService rechargeOrderService;

  @PostMapping("/handle")
  @PreAuthorize("hasAuthority('finance:rechargeOrder:handle')")
  public void handle(Long id) {
    rechargeOrderService.handle(id, SecurityUserHolder.getCredential());
  }

  @PostMapping("/unHandle")
  @PreAuthorize("hasAuthority('finance:rechargeOrder:unHandle')")
  public void unHandle(Long id) {
    UserCredential userCredential = SecurityUserHolder.getCredential();
    rechargeOrderService.unHandle(id, userCredential);
  }

  @PostMapping("/accept")
  @PreAuthorize("hasAuthority('finance:rechargeOrder:accept')")
  public void accept(Long id) {
    try {
      UserCredential userCredential = SecurityUserHolder.getCredential();
      rechargeOrderService.accept(id, userCredential);
    } catch (Exception e) {
      log.info("入款失败，异常信息" + e);
    }
  }

  @PostMapping("/cancel")
  @PreAuthorize("hasAuthority('finance:rechargeOrder:cancel')")
  public void cancel(Long id) {
    UserCredential userCredential = SecurityUserHolder.getCredential();
    rechargeOrderService.cancel(id, userCredential);
  }

  @PostMapping("/batchHandle")
  @PreAuthorize("hasAuthority('finance:rechargeOrder:handle')")
  public void batchHandle(List<Long> ids) {
    if (null == ids || ids.size() == 0) {
      throw new ServiceException("ids不能为空");
    }
    UserCredential userCredential = SecurityUserHolder.getCredential();
    for (Long id : ids) {
      rechargeOrderService.handle(id, userCredential);
    }
  }

  @PostMapping("/batchUnHandle")
  @PreAuthorize("hasAuthority('finance:rechargeOrder:unHandle')")
  public void batchUnHandle(List<Long> ids) {
    if (null == ids || ids.size() == 0) {
      throw new ServiceException("ids不能为空");
    }
    UserCredential userCredential = SecurityUserHolder.getCredential();
    for (Long id : ids) {
      rechargeOrderService.unHandle(id, userCredential);
    }
  }

  @PostMapping("/batchAccept")
  @PreAuthorize("hasAuthority('finance:rechargeOrder:accept')")
  public void batchAccept(List<Long> ids) {
    if (null == ids || ids.size() == 0) {
      throw new ServiceException("ids不能为空");
    }
    try {
      UserCredential userCredential = SecurityUserHolder.getCredential();
      for (Long id : ids) {
        rechargeOrderService.accept(id, userCredential);
      }
    } catch (Exception e) {
      log.info("批量入款失败，异常信息" + e);
    }
  }

  @PostMapping("/batchCancel")
  @PreAuthorize("hasAuthority('finance:rechargeOrder:cancel')")
  public void batchCancel(List<Long> ids) {
    if (null == ids || ids.size() == 0) {
      throw new ServiceException("ids不能为空");
    }
    UserCredential userCredential = SecurityUserHolder.getCredential();
    for (Long id : ids) {
      rechargeOrderService.cancel(id, userCredential);
    }
  }

  @PostMapping("/editDiscount")
  @PreAuthorize("hasAuthority('finance:rechargeOrder:editDiscount')")
  public void updateDiscount(
      Long id, Integer discountType, BigDecimal discountAmount, BigDecimal discountDml) {
    rechargeOrderService.updateDiscount(id, discountType, discountAmount, discountDml);
  }

  @PostMapping("/editRemarks")
  @PreAuthorize("hasAuthority('finance:rechargeOrder:editRemarks')")
  public void updateRemarks(Long id, String auditRemarks) {
    rechargeOrderService.updateRemarks(id, auditRemarks);
  }

  @PostMapping("/page")
  @PreAuthorize("hasAuthority('finance:rechargeOrder:page')")
  public IPage<RechargeOrderVO> queryPage(Page<RechargeOrder> page, RechargeOrderQueryDTO dto) {
    return rechargeOrderService.findPage(page, dto);
  }

  @PostMapping("/manual")
  @PreAuthorize("hasAuthority('finance:rechargeOrder:manual')")
  public void manual(ManualRechargeOrderBo manualRechargeOrderBo) throws Exception {
    UserCredential userCredential = SecurityUserHolder.getCredential();
    rechargeOrderService.manual(manualRechargeOrderBo, userCredential);
  }
}
