package com.gameplat.admin.convert;

import com.gameplat.admin.model.vo.MemberLoanVO;
import com.gameplat.model.entity.member.MemberLoan;
import org.mapstruct.Mapper;

/**
 * @author lily
 * @description
 * @date 2022/3/6
 */
@Mapper(componentModel = "spring")
public interface MemberLoanConvert {

    MemberLoanVO toVo(MemberLoan entity);

}
