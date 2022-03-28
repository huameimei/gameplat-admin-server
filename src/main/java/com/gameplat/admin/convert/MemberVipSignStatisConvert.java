package com.gameplat.admin.convert;

import com.gameplat.admin.model.vo.MemberVipSignStatisVO;
import com.gameplat.model.entity.member.MemberVipSignStatis;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberVipSignStatisConvert {

  MemberVipSignStatisVO toVo(MemberVipSignStatis entity);
}
