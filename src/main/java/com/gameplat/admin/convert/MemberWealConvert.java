package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.MemberWeal;
import com.gameplat.admin.model.dto.MemberWealAddDTO;
import com.gameplat.admin.model.vo.MemberWealVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberWealConvert {

  MemberWealVO toVo(MemberWeal weal);

  MemberWeal toEntity(MemberWealAddDTO addDTO);

//  MemberWeal toEntity(MemberWealEditDTO editDTO);
}
