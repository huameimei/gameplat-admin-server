package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.MemberLevel;
import com.gameplat.admin.model.dto.MemberLevelAddDTO;
import com.gameplat.admin.model.dto.MemberLevelEditDTO;
import com.gameplat.admin.model.vo.MemberLevelVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberLevelConvert {

  MemberLevelVO toVo(MemberLevel entity);

  MemberLevel toEntity(MemberLevelAddDTO dto);

  MemberLevel toEntity(MemberLevelEditDTO dto);
}
