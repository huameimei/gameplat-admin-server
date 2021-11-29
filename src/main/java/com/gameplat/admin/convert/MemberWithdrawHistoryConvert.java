package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.MemberWithdrawHistory;
import com.gameplat.admin.model.dto.MemberWithdrawHistoryQueryDTO;
import com.gameplat.admin.model.vo.MemberWithdrawHistoryVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberWithdrawHistoryConvert {

  MemberWithdrawHistoryVO toVo(MemberWithdrawHistory entity);

  MemberWithdrawHistory toEntity(MemberWithdrawHistoryQueryDTO dto);

}
