package com.gameplat.admin.convert;

import com.gameplat.admin.model.vo.MemberGrowthStatisVO;
import com.gameplat.model.entity.member.MemberGrowthStatis;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberGrowthStatisConvert {

  MemberGrowthStatisVO toVo(MemberGrowthStatis statis);
}
