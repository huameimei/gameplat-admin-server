package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.Member;
import com.gameplat.admin.model.dto.MemberAddDTO;
import com.gameplat.admin.model.dto.MemberContactUpdateDTO;
import com.gameplat.admin.model.dto.MemberEditDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberConvert {

  Member toEntity(MemberAddDTO dto);

  Member toEntity(MemberEditDTO dto);

  Member toEntity(MemberContactUpdateDTO dto);
}
