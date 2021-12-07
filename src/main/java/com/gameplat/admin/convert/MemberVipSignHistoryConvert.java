package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.MemberVipSignHistory;
import com.gameplat.admin.model.vo.MemberVipSignHistoryVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberVipSignHistoryConvert{

    MemberVipSignHistoryVO toVo(MemberVipSignHistory entity);
}
