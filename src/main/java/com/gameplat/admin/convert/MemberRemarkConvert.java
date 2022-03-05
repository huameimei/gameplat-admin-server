package com.gameplat.admin.convert;

import com.gameplat.admin.model.vo.MemberRemarkVO;
import com.gameplat.model.entity.member.MemberRemark;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberRemarkConvert {

  MemberRemarkVO toVo(MemberRemark entity);
}
