package com.gameplat.admin.controller.open.finance;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.bean.*;
import com.gameplat.admin.model.dto.ManualRechargeOrderDto;
import com.gameplat.admin.model.dto.RechargeOrderQueryDTO;
import com.gameplat.admin.model.vo.*;
import com.gameplat.admin.service.GameTransferRecordService;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Tag(name = "充值订单")
@RestController
@RequestMapping("/api/admin/finance/rechargeOrder")
public class RechargeOrderController {

  @Autowired private RechargeOrderService rechargeOrderService;

  @Autowired private MemberWithdrawService memberWithdrawService;

  @Autowired private GameTransferRecordService gameTransferRecordService;

  @Autowired private DistributedLocker distributedLocker;

  @Operation(summary = "受理")
  @PostMapping("/handle")
  @PreAuthorize("hasAuthority('finance:rechargeOrder:handle')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.RECHARGE, desc = "'受理订单：' + #id")
  public void handle(Long id, Long memberId) {
    String lockKey = "member_rw_" + memberId;
    distributedLocker.lock(lockKey);

    try {
      rechargeOrderService.handle(id);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      distributedLocker.unlock(lockKey);
    }
  }

  @Operation(summary = "取消受理订单")
  @PostMapping("/unHandle")
  @PreAuthorize("hasAuthority('finance:rechargeOrder:unHandle')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.RECHARGE, desc = "'取消受理订单：' + #id")
  public void unHandle(Long id, Long memberId) {
    String lockKey = "member_rw_" + memberId;
    distributedLocker.lock(lockKey);

    try {
      rechargeOrderService.unHandle(id);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      distributedLocker.unlock(lockKey);
    }
  }

  @Operation(summary = "入款")
  @PostMapping("/accept")
  @PreAuthorize("hasAuthority('finance:rechargeOrder:accept')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.RECHARGE, desc = "'入款订单：' + #id")
  public void accept(Long id, Long memberId) {
    String lockKey = "member_rw_" + memberId;
    distributedLocker.lock(lockKey);

    try {
      rechargeOrderService.accept(id, null, true);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      distributedLocker.unlock(lockKey);
    }
  }

  @Operation(summary = "取消")
  @PostMapping("/cancel")
  @PreAuthorize("hasAuthority('finance:rechargeOrder:cancel')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.RECHARGE, desc = "'取消订单：' + #id")
  public void cancel(Long id, Long memberId) {
    String lockKey = "member_rw_" + memberId;
    distributedLocker.lock(lockKey);

    try {
      rechargeOrderService.cancel(id);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      distributedLocker.unlock(lockKey);
    }
  }

  @Operation(summary = "批量受理订单")
  @PostMapping("/batchHandle")
  @PreAuthorize("hasAuthority('finance:rechargeOrder:handle')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.RECHARGE, desc = "'批量受理订单：' + #ids")
  public void batchHandle(@RequestParam List<Long> ids) {
    if (CollUtil.isEmpty(ids)) {
      throw new ServiceException("ids不能为空");
    }

    String lockKey = "member_rw_single";
    distributedLocker.lock(lockKey);

    try {
      ids.forEach(id -> rechargeOrderService.handle(id));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      distributedLocker.unlock(lockKey);
    }
  }

  @Operation(summary = "批量取消受理订单")
  @PostMapping("/batchUnHandle")
  @PreAuthorize("hasAuthority('finance:rechargeOrder:unHandle')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.RECHARGE, desc = "'批量取消受理订单：' + #ids")
  public void batchUnHandle(@RequestParam List<Long> ids) {
    if (CollUtil.isEmpty(ids)) {
      throw new ServiceException("ids不能为空");
    }

    String lockKey = "member_rw_single";
    distributedLocker.lock(lockKey);

    try {
      ids.forEach(id -> rechargeOrderService.unHandle(id));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      distributedLocker.unlock(lockKey);
    }
  }

  @Operation(summary = "批量入款")
  @PostMapping("/batchAccept")
  @PreAuthorize("hasAuthority('finance:rechargeOrder:accept')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.RECHARGE, desc = "'批量入款订单：' + #ids")
  public void batchAccept(@RequestParam List<Long> ids) {
    if (CollUtil.isEmpty(ids)) {
      throw new ServiceException("ids不能为空");
    }

    String lockKey = "member_rw_single";
    distributedLocker.lock(lockKey);

    try {
      for (Long id : ids) {
        rechargeOrderService.accept(id, null, true);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      distributedLocker.unlock(lockKey);
    }
  }

  @Operation(summary = "批量取消订单")
  @PostMapping("/batchCancel")
  @PreAuthorize("hasAuthority('finance:rechargeOrder:cancel')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.RECHARGE, desc = "'批量取消订单：' + #ids")
  public void batchCancel(@RequestParam List<Long> ids) {
    if (CollUtil.isEmpty(ids)) {
      throw new ServiceException("ids不能为空");
    }

    String lockKey = "member_rw_single";
    distributedLocker.lock(lockKey);

    try {
      for (Long id : ids) {
        rechargeOrderService.cancel(id);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      distributedLocker.unlock(lockKey);
    }
  }

  @Operation(summary = "修改优惠金额")
  @PostMapping("/editDiscount")
  @PreAuthorize("hasAuthority('finance:rechargeOrder:editDiscount')")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.RECHARGE,
      desc = "'修改优惠金额为：' + #discountAmount")
  public void updateDiscount(
      Long id,
      Integer discountType,
      BigDecimal discountAmount,
      BigDecimal discountDml,
      Long memberId) {
    String lockKey = "member_rw_" + memberId;
    distributedLocker.lock(lockKey);

    try {
      rechargeOrderService.updateDiscount(id, discountType, discountAmount, discountDml);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      distributedLocker.unlock(lockKey);
    }
  }

  @Operation(summary = "修改备注")
  @PostMapping("/editRemarks")
  @PreAuthorize("hasAuthority('finance:rechargeOrder:editRemarks')")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.RECHARGE,
      desc = "'修改备注：' + #auditRemarks")
  public void updateRemarks(Long id, String auditRemarks) {
    rechargeOrderService.updateRemarks(id, auditRemarks);
  }

  @Operation(summary = "受理")
  @PostMapping("/page")
  @PreAuthorize("hasAuthority('finance:rechargeOrder:view')")
  public PageExt<RechargeOrderVO, SummaryVO> queryPage(
      Page<RechargeOrder> page, RechargeOrderQueryDTO dto) {
    return rechargeOrderService.findPage(page, dto);
  }

  @SneakyThrows
  @Operation(summary = "人工入款")
  @PostMapping("/manual")
  @PreAuthorize("hasAuthority('finance:rechargeOrder:manual')")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.RECHARGE,
      desc = "'人工入款memberId：' + #manualRechargeOrderBo.memberId")
  public void manual(ManualRechargeOrderBo manualRechargeOrderBo, HttpServletRequest request) {
    String lockKey = "recharge_manual_" + SecurityUserHolder.getUserId();
    UserEquipment clientInfo = UserEquipment.create(request);
    distributedLocker.lock(lockKey);

    try {
      rechargeOrderService.manual(manualRechargeOrderBo, clientInfo);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      distributedLocker.unlock(lockKey);
    }
  }

  @Operation(summary = "获取未处理数据统计数据")
  @GetMapping("/withdraw_charge")
  public WithdrawChargeVO getTotal() {
    WithdrawChargeVO vo = new WithdrawChargeVO();
    // 未受理充值订单
    long rechargeCount = rechargeOrderService.getUntreatedRechargeCount();
    // 未受理提现订单
    long withdrawCount = memberWithdrawService.getUntreatedWithdrawCount();
    // 额度流水补发
    long warningCount = gameTransferRecordService.findGameTransferFailRecord();
    vo.setRechargeCount(rechargeCount);
    vo.setWithdrawCount(withdrawCount);
    vo.setWarningCount(warningCount);
    return vo;
  }

  @SneakyThrows
  @Operation(summary = "人工入款批量充值")
  @PostMapping("/fileUserNameRech")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.RECHARGE, desc = "'人工入款批量充值")
  @PreAuthorize("hasAuthority('finance:rechargeOrder:batchFileRecharge')")
  public void fileUserNameRech(
      @RequestPart(value = "file", required = false) MultipartFile file,
      ManualRechargeOrderDto dto,
      HttpServletRequest request) {
    log.info("入参：{}", JSONUtil.parse(dto));
    // 开始批量解析文件
    List<RechargeMemberFileBean> strAccount = null;
    if (file != null) {
      MemberRechBatchListener memberRechBatchListener = new MemberRechBatchListener();
      EasyExcel.read(file.getInputStream(), RechargeMemberFileBean.class, memberRechBatchListener)
              .sheet()
              .doRead();
      strAccount = memberRechBatchListener.getVoList();
      log.info("会员账号数据：{}", strAccount.size());
      if (CollUtil.isEmpty(strAccount)) {
        return;
      }
    }
    UserEquipment clientInfo = UserEquipment.create(request);
    rechargeOrderService.batchMemberRecharge(clientInfo, strAccount, dto);
  }

  @SneakyThrows
  @Operation(summary = "人工入款文件批量充值")
  @PostMapping("/fileRech")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.RECHARGE, desc = "'人工入款文件批量充值")
  @PreAuthorize("hasAuthority('finance:rechargeOrder:batchRecharge')")
  public void fileRech(
      @RequestPart(value = "file", required = false) MultipartFile file,
      @RequestParam Integer discountType,
      HttpServletRequest request) {
    // 开始批量解析文件
    MemberRechListener memberRechListener = new MemberRechListener();
    EasyExcel.read(file.getInputStream(), MemberRechBalanceVO.class, memberRechListener)
            .sheet()
            .doRead();
    List<MemberRechBalanceVO> voList = memberRechListener.getVoList();
    log.info("批量上传数据：{}", voList.size());

    UserEquipment userEquipment = UserEquipment.create(request);
    rechargeOrderService.batchFileMemberRecharge(voList, discountType, userEquipment);
  }
}
