package com.gameplat.admin.controller.open.finance;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.bean.ManualRechargeOrderBo;
import com.gameplat.admin.model.bean.PageExt;
import com.gameplat.admin.model.dto.ManualRechargeOrderDto;
import com.gameplat.admin.model.dto.RechargeOrderQueryDTO;
import com.gameplat.admin.model.vo.RechargeOrderVO;
import com.gameplat.admin.model.vo.SummaryVO;
import com.gameplat.admin.service.RechargeOrderService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.common.model.bean.UserEquipment;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.recharge.RechargeOrder;
import com.gameplat.security.SecurityUserHolder;
import com.gameplat.security.context.UserCredential;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/admin/finance/rechargeOrder")
public class RechargeOrderController {

  @Autowired private RechargeOrderService rechargeOrderService;

  @PostMapping("/handle")
  @PreAuthorize("hasAuthority('finance:rechargeOrder:handle')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.RECHARGE, desc = "'受理订单：' + #id")
  public void handle(Long id) {
    UserCredential userCredential = SecurityUserHolder.getCredential();
    rechargeOrderService.handle(id, userCredential);
  }

  @PostMapping("/unHandle")
  @PreAuthorize("hasAuthority('finance:rechargeOrder:unHandle')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.RECHARGE, desc = "'取消受理订单：' + #id")
  public void unHandle(Long id) {
    UserCredential userCredential = SecurityUserHolder.getCredential();
    rechargeOrderService.unHandle(id, userCredential);
  }

  @PostMapping("/accept")
  @PreAuthorize("hasAuthority('finance:rechargeOrder:accept')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.RECHARGE, desc = "'入款订单：' + #id")
  public void accept(Long id) throws Exception {
    UserCredential userCredential = SecurityUserHolder.getCredential();
    rechargeOrderService.accept(id, userCredential, null);
  }

  @PostMapping("/cancel")
  @PreAuthorize("hasAuthority('finance:rechargeOrder:cancel')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.RECHARGE, desc = "'取消订单：' + #id")
  public void cancel(Long id) {
    UserCredential userCredential = SecurityUserHolder.getCredential();
    rechargeOrderService.cancel(id, userCredential);
  }

  @PostMapping("/batchHandle")
  @PreAuthorize("hasAuthority('finance:rechargeOrder:handle')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.RECHARGE, desc = "'批量受理订单：' + #ids")
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
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.RECHARGE, desc = "'批量取消受理订单：' + #ids")
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
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.RECHARGE, desc = "'批量入款订单：' + #ids")
  public void batchAccept(List<Long> ids) throws Exception {
    if (null == ids || ids.size() == 0) {
      throw new ServiceException("ids不能为空");
    }
    UserCredential userCredential = SecurityUserHolder.getCredential();
    for (Long id : ids) {
      rechargeOrderService.accept(id, userCredential, null);
    }
  }

  @PostMapping("/batchCancel")
  @PreAuthorize("hasAuthority('finance:rechargeOrder:cancel')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.RECHARGE, desc = "'批量取消订单：' + #ids")
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
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.RECHARGE, desc = "'修改优惠金额为：' + #discountAmount")
  public void updateDiscount(
      Long id, Integer discountType, BigDecimal discountAmount, BigDecimal discountDml) {
    rechargeOrderService.updateDiscount(id, discountType, discountAmount, discountDml);
  }

  @PostMapping("/editRemarks")
  @PreAuthorize("hasAuthority('finance:rechargeOrder:editRemarks')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.RECHARGE, desc = "'修改备注：' + #auditRemarks")
  public void updateRemarks(Long id, String auditRemarks) {
    rechargeOrderService.updateRemarks(id, auditRemarks);
  }

  @PostMapping("/page")
  @PreAuthorize("hasAuthority('finance:rechargeOrder:page')")
  public PageExt<RechargeOrderVO, SummaryVO> queryPage(Page<RechargeOrder> page,
      RechargeOrderQueryDTO dto) {
    return rechargeOrderService.findPage(page, dto);
  }

  @PostMapping("/manual")
  @PreAuthorize("hasAuthority('finance:rechargeOrder:manual')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.RECHARGE, desc = "'人工入款memberId：' + #manualRechargeOrderBo.memberId")
  public void manual(ManualRechargeOrderBo manualRechargeOrderBo, HttpServletRequest request)
      throws Exception {
    UserCredential userCredential = SecurityUserHolder.getCredential();
    UserEquipment clientInfo = UserEquipment.create(request);
    rechargeOrderService.manual(manualRechargeOrderBo, userCredential, clientInfo);
  }


  /**
   * 人工批量充值（文件上传 username）
   */
  @PostMapping("/fileUserNameRech")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.RECHARGE, desc = "'人工入款批量充值")
  public void fileUserNameRech(ManualRechargeOrderDto dto, @RequestPart(value = "file",required = false) MultipartFile file, HttpServletRequest request) throws Exception {
    log.info("根据用户名进行批量充值操作人：{}", SecurityUserHolder.getCredential().getUsername());
    rechargeOrderService.fileUserNameRech(dto,file,request,SecurityUserHolder.getCredential());
  }


  /**
   * 人工批量充值（文件上传）
   */
  @PostMapping("/fileRech")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.RECHARGE, desc = "'人工入款文件批量充值")
  public void fileRech(@RequestPart(value = "file",required = false) MultipartFile file,@RequestParam("discountType") Integer discountType, HttpServletRequest request) throws Exception {
    log.info("根据上传文件进行批量充值操作人：{}", SecurityUserHolder.getCredential().getUsername());
    rechargeOrderService.fileRech(file,discountType,request,SecurityUserHolder.getCredential());
  }



}
