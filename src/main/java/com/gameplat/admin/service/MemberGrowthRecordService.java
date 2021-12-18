package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.bean.ActivityMemberInfo;
import com.gameplat.admin.model.domain.MemberGrowthRecord;
import com.gameplat.admin.model.domain.MemberGrowthStatis;
import com.gameplat.admin.model.dto.MemberGrowthRecordDTO;
import com.gameplat.admin.model.dto.MemberGrowthStatisDTO;
import com.gameplat.admin.model.vo.MemberGrowthConfigVO;
import com.gameplat.admin.model.vo.MemberGrowthRecordVO;

import java.util.List;
import java.util.Map;


public interface MemberGrowthRecordService extends IService<MemberGrowthRecord> {

    IPage<MemberGrowthRecordVO> findRecordList(PageDTO<MemberGrowthRecord> page, MemberGrowthRecordDTO dto);

    Integer dealUpLevel(Integer afterGrowth, MemberGrowthConfigVO memberGrowthConfig);

    List<ActivityMemberInfo> findActivityMemberInfo(Map map);

    List<MemberGrowthRecord> findRecordGroupBy(MemberGrowthRecord entity);
}
