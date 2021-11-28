package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.MemberWealDetail;
import com.gameplat.admin.model.vo.MemberWealDetailVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberWealDetailConvert {

    MemberWealDetailVO toVo(MemberWealDetail entity);

}
