package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.MemberBankAddDTO;
import com.gameplat.admin.model.dto.MemberBankEditDTO;
import com.gameplat.admin.model.vo.MemberBankVO;
import com.gameplat.model.entity.member.MemberBank;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberBankConvert {

  MemberBankVO toVo(MemberBank entity);

  MemberBank toEntity(MemberBankAddDTO dto);

  MemberBank toEntity(MemberBankEditDTO dto);
}
