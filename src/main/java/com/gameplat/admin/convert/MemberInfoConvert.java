package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.MemberInfoAddDto;
import com.gameplat.admin.model.dto.MemberInfoEditDto;
import com.gameplat.admin.model.entity.MemberInfo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberInfoConvert {

  MemberInfo toEntity(MemberInfoAddDto dto);

  MemberInfo toEntity(MemberInfoEditDto dto);
}
