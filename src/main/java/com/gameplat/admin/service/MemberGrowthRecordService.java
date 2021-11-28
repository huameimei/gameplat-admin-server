package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.MemberGrowthRecord;
import com.gameplat.admin.model.dto.MemberGrowthRecordDTO;
import com.gameplat.admin.model.vo.MemberGrowthRecordVO;


public interface MemberGrowthRecordService extends IService<MemberGrowthRecord> {

    IPage<MemberGrowthRecordVO> findRecordList(PageDTO<MemberGrowthRecord> page, MemberGrowthRecordDTO dto);
}
