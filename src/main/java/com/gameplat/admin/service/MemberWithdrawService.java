package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.MemberWithdrawDTO;
import com.gameplat.admin.model.dto.MemberWithdrawQueryDTO;
import com.gameplat.admin.model.vo.MemberWithdrawVO;
import com.gameplat.common.enums.WithdrawStatus;
import com.gameplat.common.model.bean.UserEquipment;
import com.gameplat.model.entity.member.MemberWithdraw;
import com.gameplat.model.entity.pay.PpMerchant;

import java.math.BigDecimal;
import java.util.List;

public interface MemberWithdrawService extends IService<MemberWithdraw> {

  IPage<MemberWithdrawVO> findPage(Page<MemberWithdraw> page, MemberWithdrawQueryDTO dto);

  void updateCounterFee(Long id, BigDecimal afterCounterFee);

  void updateRemarks(Long id, String remarks);

  void modify(
          Long id,
          Integer cashStatus,
          Integer curStatus,
          UserEquipment userEquipment,
          String cashReason);

  void batchModify(
          List<MemberWithdrawDTO> dtoList,
          WithdrawStatus cashStatus,
          UserEquipment userEquipment,
          String cashReason);

  List<PpMerchant> queryProxyMerchant(Long id);

  void save(BigDecimal cashMoney, String cashReason, Integer handPoints, Long memberId);

  long getUntreatedWithdrawCount();
}
