package com.gameplat.admin.controller.open.finance;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.bean.ManualRechargeOrderBo;
import com.gameplat.admin.model.bean.PageExt;
import com.gameplat.admin.model.dto.RechargeOrderQueryDTO;
import com.gameplat.admin.model.vo.RechargeOrderVO;
import com.gameplat.admin.model.vo.SummaryVO;
import com.gameplat.admin.model.vo.WithdrawChargeVO;
import com.gameplat.admin.service.MemberWithdrawService;
import com.gameplat.admin.service.RechargeOrderService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.common.model.bean.UserEquipment;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.recharge.RechargeOrder;
import com.gameplat.redis.redisson.DistributedLocker;
import com.gameplat.security.SecurityUserHolder;
import com.gameplat.security.context.UserCredential;
import io.swagger.annotations.ApiOperation;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/admin/finance/rechargeOrder")
public class RechargeOrderController {

  @Autowired
  private RechargeOrderService rechargeOrderService;
  @Autowired
  private MemberWithdrawService memberWithdrawService;
  @Autowired
  private DistributedLocker distributedLocker;

  @PostMapping("/handle")
  @PreAuthorize("hasAuthority('finance:rechargeOrder:handle')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.RECHARGE, desc = "'受理订单：' + #id")
  public void handle(Long id, Long memberId) {
    String lock_key = "member_rw_" + memberId;
    distributedLocker.lock(lock_key);
    try {
      UserCredential userCredential = SecurityUserHolder.getCredential();
      rechargeOrderService.handle(id, userCredential);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      distributedLocker.unlock(lock_key);
    }
  }

  @PostMapping("/unHandle")
  @PreAuthorize("hasAuthority('finance:rechargeOrder:unHandle')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.RECHARGE, desc = "'取消受理订单：' + #id")
  public void unHandle(Long id, Long memberId) {
    String lock_key = "member_rw_" + memberId;
    distributedLocker.lock(lock_key);
    try {
      UserCredential userCredential = SecurityUserHolder.getCredential();
      rechargeOrderService.unHandle(id, userCredential);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      distributedLocker.unlock(lock_key);
    }
  }

  @PostMapping("/accept")
  @PreAuthorize("hasAuthority('finance:rechargeOrder:accept')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.RECHARGE, desc = "'入款订单：' + #id")
  public void accept(Long id, Long memberId) throws Exception {
    String lock_key = "member_rw_" + memberId;
    distributedLocker.lock(lock_key);
    try {
      UserCredential userCredential = SecurityUserHolder.getCredential();
      rechargeOrderService.accept(id, userCredential, null, true);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      distributedLocker.unlock(lock_key);
    }
  }

  @PostMapping("/cancel")
  @PreAuthorize("hasAuthority('finance:rechargeOrder:cancel')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.RECHARGE, desc = "'取消订单：' + #id")
  public void cancel(Long id, Long memberId) {
    String lock_key = "member_rw_" + memberId;
    distributedLocker.lock(lock_key);
    try {
      UserCredential userCredential = SecurityUserHolder.getCredential();
      rechargeOrderService.cancel(id, userCredential);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      distributedLocker.unlock(lock_key);
    }
  }

  @PostMapping("/batchHandle")
  @PreAuthorize("hasAuthority('finance:rechargeOrder:handle')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.RECHARGE, desc = "'批量受理订单：' + #ids")
  public void batchHandle(@RequestParam List<Long> ids) {
    String lock_key = "member_rw_single";
    distributedLocker.lock(lock_key);
    try {
      if (null == ids || ids.size() == 0) {
        throw new ServiceException("ids不能为空");
      }
      UserCredential userCredential = SecurityUserHolder.getCredential();
      for (Long id : ids) {
        rechargeOrderService.handle(id, userCredential);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      distributedLocker.unlock(lock_key);
    }
  }

  @PostMapping("/batchUnHandle")
  @PreAuthorize("hasAuthority('finance:rechargeOrder:unHandle')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.RECHARGE, desc = "'批量取消受理订单：' + #ids")
  public void batchUnHandle(@RequestParam List<Long> ids) {
    String lock_key = "member_rw_single";
    distributedLocker.lock(lock_key);
    try {
      if (null == ids || ids.size() == 0) {
        throw new ServiceException("ids不能为空");
      }
      UserCredential userCredential = SecurityUserHolder.getCredential();
      for (Long id : ids) {
        rechargeOrderService.unHandle(id, userCredential);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      distributedLocker.unlock(lock_key);
    }
  }

  @PostMapping("/batchAccept")
  @PreAuthorize("hasAuthority('finance:rechargeOrder:accept')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.RECHARGE, desc = "'批量入款订单：' + #ids")
  public void batchAccept(@RequestParam List<Long> ids) throws Exception {
    String lock_key = "member_rw_single";
    distributedLocker.lock(lock_key);
    try {
      if (null == ids || ids.size() == 0) {
        throw new ServiceException("ids不能为空");
      }
      UserCredential userCredential = SecurityUserHolder.getCredential();
      for (Long id : ids) {
        rechargeOrderService.accept(id, userCredential, null, true);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      distributedLocker.unlock(lock_key);
    }
  }

  @PostMapping("/batchCancel")
  @PreAuthorize("hasAuthority('finance:rechargeOrder:cancel')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.RECHARGE, desc = "'批量取消订单：' + #ids")
  public void batchCancel(@RequestParam List<Long> ids) {
    String lock_key = "member_rw_single";
    distributedLocker.lock(lock_key);
    try {
      if (null == ids || ids.size() == 0) {
        throw new ServiceException("ids不能为空");
      }
      UserCredential userCredential = SecurityUserHolder.getCredential();
      for (Long id : ids) {
        rechargeOrderService.cancel(id, userCredential);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      distributedLocker.unlock(lock_key);
    }
  }

  @PostMapping("/editDiscount")
  @PreAuthorize("hasAuthority('finance:rechargeOrder:editDiscount')")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.RECHARGE,
      desc = "'修改优惠金额为：' + #discountAmount")
  public void updateDiscount(
      Long id, Integer discountType, BigDecimal discountAmount, BigDecimal discountDml,
      Long memberId) {
    String lock_key = "member_rw_" + memberId;
    distributedLocker.lock(lock_key);
    try {
      rechargeOrderService.updateDiscount(id, discountType, discountAmount, discountDml);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      distributedLocker.unlock(lock_key);
    }
  }

  @PostMapping("/editRemarks")
  @PreAuthorize("hasAuthority('finance:rechargeOrder:editRemarks')")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.RECHARGE,
      desc = "'修改备注：' + #auditRemarks")
  public void updateRemarks(Long id, String auditRemarks) {
    rechargeOrderService.updateRemarks(id, auditRemarks);
  }

  @PostMapping("/page")
  @PreAuthorize("hasAuthority('finance:rechargeOrder:view')")
  public PageExt<RechargeOrderVO, SummaryVO> queryPage(
      Page<RechargeOrder> page, RechargeOrderQueryDTO dto) {
    return rechargeOrderService.findPage(page, dto);
  }

  @SneakyThrows
  @PostMapping("/manual")
  @PreAuthorize("hasAuthority('finance:rechargeOrder:manual')")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.RECHARGE,
      desc = "'人工入款memberId：' + #manualRechargeOrderBo.memberId")
  public void manual(ManualRechargeOrderBo manualRechargeOrderBo, HttpServletRequest request) {
    UserCredential userCredential = SecurityUserHolder.getCredential();
    String lock_key = "recharge_manual_" + userCredential.getUserId();
    UserEquipment clientInfo = UserEquipment.create(request);
    distributedLocker.lock(lock_key);
    try {
      rechargeOrderService.manual(manualRechargeOrderBo, userCredential, clientInfo);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      distributedLocker.unlock(lock_key);
    }
  }


  @ApiOperation(value = "获取未处理数据统计数据")
  @GetMapping("/withdraw_charge")
  public WithdrawChargeVO getTotal() {
    WithdrawChargeVO vo = new WithdrawChargeVO();
    long rechargeCount = rechargeOrderService.getUntreatedRechargeCount();
    long withdrawCount = memberWithdrawService.getUntreatedWithdrawCount();
    vo.setRechargeCount(rechargeCount);
    vo.setWithdrawCount(withdrawCount);
    return vo;
  }
}
