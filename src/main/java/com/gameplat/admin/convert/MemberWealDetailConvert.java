package com.gameplat.admin.convert;

import com.gameplat.admin.model.vo.MemberWealDetailVO;
import com.gameplat.model.entity.member.MemberWealDetail;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberWealDetailConvert {

  MemberWealDetailVO toVo(MemberWealDetail entity);
}
