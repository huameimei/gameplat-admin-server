package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.MemberRemark;
import com.gameplat.admin.model.vo.MemberRemarkVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberRemarkConvert {

  MemberRemarkVO toVo(MemberRemark entity);
}
