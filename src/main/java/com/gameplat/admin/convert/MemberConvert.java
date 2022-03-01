package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.MemberAddDTO;
import com.gameplat.admin.model.dto.MemberContactUpdateDTO;
import com.gameplat.admin.model.dto.MemberEditDTO;
import com.gameplat.admin.model.vo.MemberVO;
import com.gameplat.admin.model.vo.MessageDistributeVO;
import com.gameplat.model.entity.member.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface MemberConvert {

  Member toEntity(MemberAddDTO dto);

  Member toEntity(MemberEditDTO dto);

  Member toEntity(MemberContactUpdateDTO dto);

  @Mappings({
    @Mapping(source = "id", target = "userId"),
    @Mapping(source = "account", target = "userAccount"),
    @Mapping(source = "vipLevel", target = "vipLevel"),
    @Mapping(source = "agentLevel", target = "agentLevel"),
    @Mapping(source = "userLevel", target = "rechargeLevel")
  })
  MessageDistributeVO toVo(MemberVO vo);
}
