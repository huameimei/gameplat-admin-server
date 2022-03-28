package com.gameplat.admin.convert;

import com.gameplat.admin.model.vo.MemberVipSignHistoryVO;
import com.gameplat.model.entity.member.MemberVipSignHistory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberVipSignHistoryConvert {

  MemberVipSignHistoryVO toVo(MemberVipSignHistory entity);
}
