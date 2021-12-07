package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.MemberVipSignHistory;
import com.gameplat.admin.model.dto.MemberVipSignHistoryDTO;
import com.gameplat.admin.model.vo.MemberVipSignHistoryVO;

/**
 * @author Lily
 */
public interface MemberVipSignHistoryService extends IService<MemberVipSignHistory> {

    IPage<MemberVipSignHistoryVO> findPageList(PageDTO<MemberVipSignHistory> page, MemberVipSignHistoryDTO dto);
}
