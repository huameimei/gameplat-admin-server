package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.MemberVipSignStatis;
import com.gameplat.admin.model.vo.MemberVipSignStatisVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberVipSignStatisConvert {

    MemberVipSignStatisVO toVo (MemberVipSignStatis entity);
}
