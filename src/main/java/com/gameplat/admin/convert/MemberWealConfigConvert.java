package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.MemberWealConfigAddDTO;
import com.gameplat.admin.model.dto.MemberWealConfigEditDTO;
import com.gameplat.admin.model.vo.MemberWealConfigVO;
import com.gameplat.model.entity.member.MemberWealConfig;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberWealConfigConvert {

  MemberWealConfigVO toVo(MemberWealConfig entity);

  MemberWealConfig toEntity(MemberWealConfigAddDTO dto);

  MemberWealConfig toEntity(MemberWealConfigEditDTO dto);
}
