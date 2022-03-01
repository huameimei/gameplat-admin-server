package com.gameplat.admin.convert;

import com.gameplat.admin.model.vo.MemberGrowthRecordVO;
import com.gameplat.model.entity.member.MemberGrowthRecord;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberGrowthRecordConvert {
    MemberGrowthRecordVO toVo (MemberGrowthRecord record);
}
