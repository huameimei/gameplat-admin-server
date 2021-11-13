package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.bean.ManualRechargeOrderBo;
import com.gameplat.admin.model.domain.RechargeOrder;
import com.gameplat.admin.model.dto.RechargeOrderQueryDTO;
import com.gameplat.admin.model.vo.RechargeOrderVO;
import com.gameplat.common.base.UserCredential;

import java.math.BigDecimal;

public interface RechargeOrderService extends IService<RechargeOrder> {

  IPage<RechargeOrderVO> findPage(Page<RechargeOrder> page,RechargeOrderQueryDTO dto);

  void updateDiscount(Long id, Integer discountType, BigDecimal discountAmount,BigDecimal discountDml);

  void updateRemarks(Long id, String auditRemarks);

  void handle(Long id,UserCredential userCredential);

  void unHandle(Long id,UserCredential userCredential);

  void accept(Long id, UserCredential userCredential) throws Exception;

  void cancel(Long id,UserCredential userCredential);

  void updateStatus(Long id,Integer curStatus,Integer newStatus,String auditorAccount);

  void manual(ManualRechargeOrderBo manualRechargeOrderBo, UserCredential userCredential)
      throws Exception;
}
