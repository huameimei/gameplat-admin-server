package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.MemberWealAddDTO;
import com.gameplat.admin.model.dto.MemberWealEditDTO;
import com.gameplat.admin.model.vo.MemberWealVO;
import com.gameplat.model.entity.member.MemberWeal;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberWealConvert {

  MemberWealVO toVo(MemberWeal weal);

  MemberWeal toEntity(MemberWealAddDTO addDTO);

  MemberWeal toEntity(MemberWealEditDTO editDTO);
}
