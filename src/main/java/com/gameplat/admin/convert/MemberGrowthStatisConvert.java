package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.MemberGrowthStatis;
import com.gameplat.admin.model.vo.MemberGrowthStatisVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberGrowthStatisConvert {

    MemberGrowthStatisVO toVo (MemberGrowthStatis statis);
}
