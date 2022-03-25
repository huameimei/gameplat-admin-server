package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.bean.PageExt;
import com.gameplat.admin.model.dto.MemberWithdrawQueryDTO;
import com.gameplat.admin.model.vo.MemberWithdrawVO;
import com.gameplat.admin.model.vo.SummaryVO;
import com.gameplat.common.model.bean.UserEquipment;
import com.gameplat.model.entity.member.MemberWithdraw;
import com.gameplat.model.entity.pay.PpMerchant;
import com.gameplat.security.context.UserCredential;

import java.math.BigDecimal;
import java.util.List;

public interface MemberWithdrawService extends IService<MemberWithdraw> {

  PageExt<MemberWithdrawVO, SummaryVO> findPage(
      Page<MemberWithdraw> page, MemberWithdrawQueryDTO dto);

  void updateCounterFee(Long id, BigDecimal afterCounterFee);

  void updateRemarks(Long id, String remarks);

  void modify(
      Long id,
      Integer cashStatus,
      Integer curStatus,
      boolean isDirect,
      UserCredential userCredential,
      UserEquipment userEquipment)
      throws Exception;

  List<PpMerchant> queryProxyMerchant(Long id);

  void save(
      BigDecimal cashMoney,
      String cashReason,
      Integer handPoints,
      UserCredential userCredential,
      Long memberId)
      throws Exception;


  long getUntreatedWithdrawCount();
}
