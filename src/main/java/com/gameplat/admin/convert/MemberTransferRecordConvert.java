package com.gameplat.admin.convert;

import com.gameplat.admin.model.vo.MemberTransferRecordVO;
import com.gameplat.model.entity.member.MemberTransferRecord;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberTransferRecordConvert {
  MemberTransferRecordVO toVo(MemberTransferRecord entity);
}
