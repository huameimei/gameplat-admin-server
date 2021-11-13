package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.MemberBank;
import com.gameplat.admin.model.dto.MemberBankAddDTO;
import com.gameplat.admin.model.dto.MemberBankEditDTO;
import com.gameplat.admin.model.vo.MemberBankVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberBankConvert {

  MemberBankVO toVo(MemberBank entity);

  MemberBank toEntity(MemberBankAddDTO dto);

  MemberBank toEntity(MemberBankEditDTO dto);
}
