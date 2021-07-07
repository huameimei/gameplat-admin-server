package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.MemberInfoAddDTO;
import com.gameplat.admin.model.dto.MemberInfoEditDTO;
import com.gameplat.admin.model.entity.MemberInfo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberInfoConvert {

  MemberInfo toEntity(MemberInfoAddDTO dto);

  MemberInfo toEntity(MemberInfoEditDTO dto);
}
