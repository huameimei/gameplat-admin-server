package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.MemberWithdrawHistory;
import com.gameplat.admin.model.dto.UserWithdrawHistoryQueryDTO;
import com.gameplat.admin.model.vo.MemberWithdrawHistoryVO;

public interface MemberWithdrawHistoryService extends IService<MemberWithdrawHistory> {

  IPage<MemberWithdrawHistoryVO> findPage(
      Page<MemberWithdrawHistory> page, UserWithdrawHistoryQueryDTO dto);
}