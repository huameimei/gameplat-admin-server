package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.MemberWithdrawHistoryQueryDTO;
import com.gameplat.admin.model.vo.MemberWithdrawHistoryVO;
import com.gameplat.admin.model.vo.MemberWithdrawReportVo;
import com.gameplat.model.entity.member.MemberWithdrawHistory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberWithdrawHistoryConvert {

  MemberWithdrawHistoryVO toVo(MemberWithdrawHistory entity);

  MemberWithdrawHistory toEntity(MemberWithdrawHistoryQueryDTO dto);

  MemberWithdrawReportVo toReportVo(MemberWithdrawHistory entity);
}
