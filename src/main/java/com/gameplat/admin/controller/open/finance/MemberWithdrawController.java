package com.gameplat.admin.controller.open.finance;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.dto.MemberWithdrawDTO;
import com.gameplat.admin.model.dto.MemberWithdrawQueryDTO;
import com.gameplat.admin.model.vo.MemberWithdrawVO;
import com.gameplat.admin.service.MemberWithdrawService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.common.enums.WithdrawStatus;
import com.gameplat.common.model.bean.UserEquipment;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.member.MemberWithdraw;
import com.gameplat.model.entity.pay.PpMerchant;
import com.gameplat.redis.redisson.DistributedLocker;
import com.gameplat.security.SecurityUserHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Tag(name = "会员提现")
@RestController
@RequestMapping("/api/admin/finance/memberWithdraw")
public class MemberWithdrawController {

  @Autowired private MemberWithdrawService userWithdrawService;

  @Autowired private DistributedLocker distributedLocker;

  @Operation(summary = "修改提现状态")
  @PostMapping("/modifyCashStatus")
  @PreAuthorize("hasAuthority('finance:memberWithdraw:modifyCashStatus')")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.WITHDRAW,
      desc = "'修改提款订单状态为:' + #cashStatus")
  public void modifyCashStatus(
      Long id, Integer cashStatus, Integer curStatus, HttpServletRequest request, Long memberId) {
    UserEquipment clientInfo = UserEquipment.create(request);
    String lockKey = "member_rw_" + memberId;
    RLock lock = distributedLocker.lock(lockKey);

    try {
      userWithdrawService.modify(id, cashStatus, curStatus, clientInfo);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      lock.unlock();
    }
  }

  @Operation(summary = "批量受理")
  @PostMapping("/batchHandle")
  @PreAuthorize("hasAuthority('finance:memberWithdraw:batchHandle')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.WITHDRAW, desc = "'批量受理订单:' + #list")
  public String batchHandle(String list, HttpServletRequest request) {
    if (null == list) {
      throw new ServiceException("批量受理请求参数为空");
    }

    UserEquipment clientInfo = UserEquipment.create(request);
    String lockKey = "member_rw_single";
    distributedLocker.lock(lockKey);

    try {
      List<MemberWithdrawDTO> dtoList = JSONUtil.toList(list, MemberWithdrawDTO.class);
      userWithdrawService.batchModify(dtoList, WithdrawStatus.HANDLED, clientInfo);
      return "成功受理" + dtoList.size() + "条订单";
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      distributedLocker.unlock(lockKey);
    }
  }

  @Operation(summary = "批量取消")
  @PostMapping("/batchUnHandle")
  @PreAuthorize("hasAuthority('finance:memberWithdraw:batchUnHandle')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.WITHDRAW, desc = "'批量取消受理订单:' + #list")
  public String batchModifyCashStatus(String list, HttpServletRequest request) {
    if (null == list) {
      throw new ServiceException("批量取消受理请求参数为空");
    }

    UserEquipment clientInfo = UserEquipment.create(request);
    String lockKey = "member_rw_single";
    RLock lock = distributedLocker.lock(lockKey);

    try {
      List<MemberWithdrawDTO> dtoList = JSONUtil.toList(list, MemberWithdrawDTO.class);
      userWithdrawService.batchModify(dtoList, WithdrawStatus.UNHANDLED, clientInfo);
      return "成功取消受理" + dtoList.size() + "条订单";
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      distributedLocker.unlock(lock);
    }
  }

  @Operation(summary = "批量出款")
  @PostMapping("/batchWithdraw")
  @PreAuthorize("hasAuthority('finance:memberWithdraw:batchWithdraw')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.WITHDRAW, desc = "'批量出款订单:' + #list")
  public String batchWithdraw(String list, HttpServletRequest request) {
    if (null == list) {
      throw new ServiceException("批量出款请求参数为空");
    }

    UserEquipment clientInfo = UserEquipment.create(request);
    String lockKey = "member_rw_single";
    distributedLocker.lock(lockKey);

    try {
      List<MemberWithdrawDTO> dtoList = JSONUtil.toList(list, MemberWithdrawDTO.class);
      userWithdrawService.batchModify(dtoList, WithdrawStatus.SUCCESS, clientInfo);
      return "成功出款" + dtoList.size() + "条订单";
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      distributedLocker.unlock(lockKey);
    }
  }

  @Operation(summary = "编辑优惠")
  @PostMapping("/editDiscount")
  @PreAuthorize("hasAuthority('finance:memberWithdraw:editDiscount')")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.WITHDRAW,
      desc = "'修改手续费为:' + #afterCounterFee")
  public void updateDiscount(Long id, BigDecimal afterCounterFee, Long memberId) {
    String lockKey = "member_rw_" + memberId;
    distributedLocker.lock(lockKey);

    try {
      userWithdrawService.updateCounterFee(id, afterCounterFee);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      distributedLocker.unlock(lockKey);
    }
  }

  @Operation(summary = "编辑备注")
  @PostMapping("/editRemarks")
  @PreAuthorize("hasAuthority('finance:memberWithdraw:editRemarks')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.WITHDRAW, desc = "'修改备注为:' + #remarks")
  public void updateRemarks(Long id, String remarks) {
    userWithdrawService.updateRemarks(id, remarks);
  }

  @Operation(summary = "查询")
  @PostMapping("/page")
  @PreAuthorize("hasAuthority('finance:memberWithdraw:view')")
  public IPage<MemberWithdrawVO> queryPage(Page<MemberWithdraw> page, MemberWithdrawQueryDTO dto) {
    return userWithdrawService.findPage(page, dto);
  }

  @Operation(summary = "获取可用的商户")
  @PostMapping("/queryAvailableMerchant")
  @PreAuthorize("hasAuthority('finance:memberWithdraw:queryAvailableMerchant')")
  public List<PpMerchant> queryAvailableMerchant(Long id) {
    return userWithdrawService.queryProxyMerchant(id);
  }

  @Operation(summary = "添加")
  @PostMapping("/save")
  @PreAuthorize("hasAuthority('finance:memberWithdraw:save')")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.WITHDRAW,
      desc = "'人工出款memberId =' + #memberId")
  public void save(BigDecimal cashMoney, String cashReason, Integer handPoints, Long memberId) {
    String lockKey = "withdraw_save_" + SecurityUserHolder.getUserId();
    distributedLocker.lock(lockKey);

    try {
      userWithdrawService.save(cashMoney, cashReason, handPoints, memberId);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      distributedLocker.unlock(lockKey);
    }
  }
}
