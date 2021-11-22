package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.bean.PageExt;
import com.gameplat.admin.model.domain.MemberWithdraw;
import com.gameplat.admin.model.domain.PpMerchant;
import com.gameplat.admin.model.dto.UserWithdrawQueryDTO;
import com.gameplat.admin.model.vo.MemberWithdrawVO;
import com.gameplat.admin.model.vo.SummaryVO;
import com.gameplat.security.context.UserCredential;
import java.math.BigDecimal;
import java.util.List;

public interface MemberWithdrawService extends IService<MemberWithdraw> {

  PageExt<MemberWithdrawVO, SummaryVO> findPage(Page<MemberWithdraw> page, UserWithdrawQueryDTO dto);

  void updateCounterFee(Long id, BigDecimal afterCounterFee);

  void updateRemarks(Long id, String remarks);

  void modify(Long id, Integer cashStatus, Integer curStatus, boolean isDirect,
      String approveReason,
      UserCredential userCredential) throws Exception;

  List<PpMerchant> queryProxyMerchant(Long id);

  void save(BigDecimal cashMoney, String cashReason, Integer handPoints,
      UserCredential userCredential, Long memberId)
      throws Exception;
}
