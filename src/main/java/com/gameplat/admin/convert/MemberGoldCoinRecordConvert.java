package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.MemberGoldCoinRecord;
import com.gameplat.admin.model.dto.MemberGoldCoinRecordAddDTO;
import com.gameplat.admin.model.dto.MemberGoldCoinRecordQueryDTO;
import com.gameplat.admin.model.vo.MemberGoldCoinRecordVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberGoldCoinRecordConvert {

    MemberGoldCoinRecord toEntity(MemberGoldCoinRecordQueryDTO dto);

    MemberGoldCoinRecord toEntity(MemberGoldCoinRecordAddDTO dto);

    MemberGoldCoinRecordVO toVo(MemberGoldCoinRecord entity);
}
