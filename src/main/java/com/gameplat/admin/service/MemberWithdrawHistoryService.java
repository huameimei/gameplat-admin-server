package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.MemberWithdrawHistoryQueryDTO;
import com.gameplat.admin.model.vo.MemberWithdrawHistorySummaryVO;
import com.gameplat.admin.model.vo.MemberWithdrawHistoryVO;
import com.gameplat.model.entity.member.MemberWithdrawHistory;

public interface MemberWithdrawHistoryService extends IService<MemberWithdrawHistory> {

  IPage<MemberWithdrawHistoryVO> findPage(
      Page<MemberWithdrawHistory> page, MemberWithdrawHistoryQueryDTO dto);

  MemberWithdrawHistorySummaryVO findSumMemberWithdrawHistory(MemberWithdrawHistoryQueryDTO dto);
}
