package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.MemberGrowthRecord;
import com.gameplat.admin.model.vo.MemberGrowthRecordVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberGrowthRecordConvert {
    MemberGrowthRecordVO toVo (MemberGrowthRecord record);
}
