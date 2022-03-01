package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.MemberGoldCoinRecord;
import com.gameplat.admin.model.dto.MemberGoldCoinRecordQueryDTO;
import com.gameplat.admin.model.vo.MemberGoldCoinRecordVO;

public interface MemberGoldCoinRecordService extends IService<MemberGoldCoinRecord> {

    /** 分页查 */
    IPage<MemberGoldCoinRecordVO> page (PageDTO<MemberGoldCoinRecord> page, MemberGoldCoinRecordQueryDTO dto);

    /** 增 */
    void add(Long memberId, Integer amount);
}
