package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.MemberBill;
import com.gameplat.admin.model.dto.MemberBillDTO;
import com.gameplat.admin.model.vo.MemberBillVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberBillConvert {

    MemberBillVO toVo(MemberBill entity);

    MemberBill toEntity(MemberBillDTO dto);

}
